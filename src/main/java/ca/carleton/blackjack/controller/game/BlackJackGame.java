package ca.carleton.blackjack.controller.game;

import ca.carleton.blackjack.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.collections.CollectionUtils.size;

/**
 * Model class for the game.
 * <p/>
 * Created by Mike on 10/7/2015.
 */
@Component
public class BlackJackGame {

    private static final Logger LOG = LoggerFactory.getLogger(BlackJackGame.class);

    private static final int DEFAULT_MAX_PLAYERS = 4;

    private int roundMaxPlayers = -1;

    private Map<WebSocketSession, Player> players;

    @PostConstruct
    public void init() {
        players = new HashMap<>();
    }

    public void startNewRound(final int numberOfPlayers) {
        this.roundMaxPlayers = numberOfPlayers;
        players.clear();
        LOG.info("Prepared new blackjack round for {} players.", numberOfPlayers);
    }

    /**
     * Register a new player in the game.
     *
     * @param session the user's session.
     * @return true if the player was added successfully.
     */
    public boolean registerPlayer(final WebSocketSession session) {
        if (size(this.players) == this.getMaxPlayers()) {
            LOG.warn("Max players already reached!");
            return false;
        }
        LOG.info("Adding {} to the game.", session.getId());
        return this.players.putIfAbsent(session, new Player()) == null;
    }

    /**
     * Remove a new player from the game.
     *
     * @param session the user's session.
     * @return true if the player was removed successfully.
     */
    public boolean deregisterPlayer(final WebSocketSession session) {
        LOG.info("Removing {} from the game.", session.getId());
        return this.players.remove(session) != null;
    }

    /**
     * Get the player sessions connected to this game.
     *
     * @return the sessions.
     */
    public Collection<WebSocketSession> getConnectedPlayers() {
        return this.players.keySet();
    }

    private int getMaxPlayers() {
        return this.roundMaxPlayers != -1 ? this.roundMaxPlayers : DEFAULT_MAX_PLAYERS;
    }
}
