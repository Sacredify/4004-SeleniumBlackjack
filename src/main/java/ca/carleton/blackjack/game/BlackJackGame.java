package ca.carleton.blackjack.game;

import ca.carleton.blackjack.game.entity.AIPlayer;
import ca.carleton.blackjack.game.entity.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections.CollectionUtils.size;

/**
 * Model class for the game.
 * <p/>
 * Created by Mike on 10/7/2015.
 */
@Component
public class BlackJackGame {

    private static final Logger LOG = LoggerFactory.getLogger(BlackJackGame.class);

    private static final Random random = new Random();

    private static final int DEFAULT_MAX_PLAYERS = 3;

    private int roundMaxPlayers = -1;

    private State gameState;

    private Map<String, Player> players;

    /**
     * The game state we're in *
     */
    public static enum State {
        WAITING_FOR_ADMIN,
        WAITING_FOR_PLAYERS,
        PLAYING
    }

    @PostConstruct
    public void init() {
        this.players = new HashMap<>();
        this.gameState = State.WAITING_FOR_ADMIN;
    }

    public void openLobby(final int numberOfPlayers) {
        if (numberOfPlayers < 1 || numberOfPlayers > 3) {
            this.roundMaxPlayers = 3;
        }
        this.roundMaxPlayers = numberOfPlayers;
        this.gameState = State.WAITING_FOR_PLAYERS;
        LOG.info("Prepared new blackjack round for {} players.", numberOfPlayers);
    }

    /**
     * Whether or not we're ready to start the game.
     *
     * @return true if the correct amount of players have joined.
     */
    public boolean readyToStart() {
        final int numberRequired = this.roundMaxPlayers == -1 ? DEFAULT_MAX_PLAYERS : this.roundMaxPlayers;
        LOG.info("Current number of players is {}. Required number is {}.", size(this.players), numberRequired);
        return size(this.players) == numberRequired;
    }

    /**
     * Populate the remaining slots with AI.
     */
    public void registerAI() {
        // EX: User enters '2' players --> this.roundmax = 2, DEFAULT = 3 ---> ADD 1 AI, need to register Dealer after.
        final int numberOfAIToAdd = this.roundMaxPlayers == -1 ? 0 : DEFAULT_MAX_PLAYERS - this.roundMaxPlayers;
        for (int i = 0; i < numberOfAIToAdd; i++) {
            this.registerPlayer(null);
        }
        this.registerDealer();
    }

    /**
     * Register a new player in the game.
     *
     * @param session the user's session.
     * @return true if the player was added successfully.
     */
    public boolean registerPlayer(final WebSocketSession session) {
        if (size(this.players) == DEFAULT_MAX_PLAYERS) {
            LOG.warn("Max players already reached!");
            return false;
        }
        if (session == null) {
            // TODO need to get actual different values not just random
            final String id = String.format("AI-%d", random.nextInt(10000));
            LOG.info("Adding AI {} to the game.", id);
            return this.players.putIfAbsent(id, new AIPlayer(null)) == null;
        } else {
            LOG.info("Adding {} to the game.", session.getId());

            if (size(this.players) == 0) {
                LOG.info("Setting first player as admin.");
                final Player admin = new Player(session);
                admin.setAdmin(true);
                return this.players.putIfAbsent(session.getId(), admin) == null;
            }

            return this.players.putIfAbsent(session.getId(), new Player(session)) == null;
        }
    }

    /**
     * Register the dealer.
     */
    public void registerDealer() {
        final AIPlayer dealer = new AIPlayer(null);
        dealer.setDealer(true);
        this.players.putIfAbsent("AI-DEALER", dealer);
        LOG.info("Added AI-DEALER to the game.");
    }

    /**
     * Remove a new player from the game.
     *
     * @param session the user's session.
     * @return true if the player was removed successfully.
     */
    public boolean deregisterPlayer(final WebSocketSession session) {
        return this.players.remove(session.getId()) != null;
    }

    /**
     * Remove all AI from the players list.
     *
     * @return true if we removed at least one.
     */
    public boolean deregisterAI() {
        final List<String> aiIds = this.players.entrySet().stream()
                .filter(entry -> !entry.getValue().isReal())
                .map(Map.Entry::getKey)
                .collect(toList());
        aiIds.forEach(this.players::remove);
        return size(aiIds) != 0;
    }

    /**
     * Get the player sessions connected to this game including AI.
     *
     * @return the sessions.
     */
    public Collection<WebSocketSession> getConnectedPlayers() {
        return this.players.values().stream()
                .map(Player::getSession)
                .collect(toList());
    }

    /**
     * Get the real players that are connected.
     *
     * @return the real players.
     */
    public Collection<Player> getConnectedRealPlayers() {
        return this.players.values().stream()
                .filter(Player::isReal)
                .collect(toList());
    }

    /**
     * Get the admin from the current list of players.
     *
     * @return the admin player.
     */
    public Player getAdmin() {
        return this.players.values().stream()
                .filter(Player::isAdmin)
                .collect(uniqueResult());
    }

    /**
     * Get the dealer from the current list of players.
     *
     * @return the dealer AI.
     */
    public Player getDealer() {
        return this.players.values().stream()
                .filter(player -> !player.isReal() && ((AIPlayer) player).isDealer())
                .collect(uniqueResult());
    }

    /**
     * Get the player for the given session.
     *
     * @param session the session.
     * @return the player.
     */
    public Player getPlayerFor(final WebSocketSession session) {
        return this.players.get(session.getId());
    }

    public boolean isWaitingForPlayers() {
        return this.gameState == State.WAITING_FOR_PLAYERS;
    }

    public boolean isPlaying() {
        return this.gameState == State.PLAYING;
    }

    private int getMaxPlayers() {
        return this.roundMaxPlayers != -1 ? this.roundMaxPlayers : DEFAULT_MAX_PLAYERS;
    }

    public static <T> Collector<T, ?, T> uniqueResult() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

}
