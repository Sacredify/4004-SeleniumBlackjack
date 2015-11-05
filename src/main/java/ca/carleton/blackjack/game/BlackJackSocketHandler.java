package ca.carleton.blackjack.game;

import ca.carleton.blackjack.game.entity.Player;
import ca.carleton.blackjack.session.SessionHandler;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static ca.carleton.blackjack.game.message.MessageUtil.Message;
import static ca.carleton.blackjack.game.message.MessageUtil.message;
import static org.apache.commons.collections.CollectionUtils.size;

/**
 * Socket handler that will contain our blackjack controls.
 * <p/>
 * Created by Mike on 10/6/2015.
 */
@Component
public class BlackJackSocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LoggerFactory.getLogger(BlackJackSocketHandler.class);

    @Autowired
    private BlackJackGame game;

    @Autowired
    private SessionHandler sessionHandler;

    /**
     * Whether or not we're accepting connections.
     */
    private boolean acceptingConnections;

    @PostConstruct
    public void init() {
        this.acceptingConnections = true;
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        LOG.info("Opened new session for {}.", session.getId());

        // For first one - disable after they join
        if (this.acceptingConnections && size(this.game.getConnectedPlayers()) == 0) {
            this.acceptingConnections = false;
        } else if (!this.acceptingConnections) {
            LOG.warn("Warning: Admin isn't accepting connections yet.");
            this.sendMessage(session, message(Message.NOT_ACCEPTING).build());
            this.sessionHandler.registerSessionForDisconnect(session);
            // Check if we're in 0 state and need to re-open
            if (size(this.game.getConnectedPlayers()) == 0) {
                this.acceptingConnections = true;
            }
            return;
        }

        if (this.game.registerPlayer(session)) {
            this.sendMessage(session, message(Message.PLAYER_CONNECTED, session.getId()).build());
            this.broadCastMessage(session, message(Message.OTHER_PLAYER_CONNECTED, session.getId()).build());

            if (this.game.getPlayerFor(session).isAdmin()) {
                LOG.info("Sending admin message to player.");
                this.sendMessage(session, message(Message.ADMIN_SET).build());
            }

            if (this.game.readyToStart()) {
                this.doReadyToStart();
            }

        } else {
            this.sendMessage(session, message(Message.NOT_ACCEPTING).build());
            this.sessionHandler.registerSessionForDisconnect(session);
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * Called after a session is closed via session.close()
     *
     * @param session the session.
     * @param status  the close status.
     */
    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        LOG.info("Closing session for {} with status {}.", session.getId(), status);

        if (this.game.deregisterPlayer(session)) {
            LOG.info("Successfully deregistered session {}.", session.getId());
            this.broadCastMessage(session, message(Message.OTHER_PLAYER_DISCONNECTED, session.getId()).build());
            // TODO Need to rollback state if necessary here.
        }

        if (this.game.isPlaying()) {
            this.broadCastMessage(session, message(Message.OTHER_PLAYER_DISCONNECTED, session.getId()).build());
        }

        // Need to deregister any existing AI if we're in a waiting state
        if (this.game.isWaitingForPlayers()) {
            if (this.game.deregisterAI()) {
                LOG.info("Deregistered existing AI.");
            }
        }
    }

    @Override
    public void handleTransportError(final WebSocketSession session, final Throwable exception)
            throws Exception {
        LOG.error("Error with the network.", exception);
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void handleTextMessage(final WebSocketSession session, final TextMessage message)
            throws Exception {
        LOG.info("Received message from {}: {}.", session.getId(), message.getPayload());

        // KEY_EXTRAVALUE1_EXTRAVALUE2
        final String[] contents = message.getPayload().split("\\|");

        switch (contents[0]) {
            case "ACCEPT":
                LOG.info("Now accepting connections.");
                this.acceptingConnections = true;
                this.game.openLobby(Integer.parseInt(contents[1]));

                // Case where we're playing with 1 person - need to start right away.
                if (this.game.readyToStart()) {
                    this.doReadyToStart();
                }
                break;
            case "START_GAME":
                LOG.info("Starting the game.");
                this.broadCastMessageFromServer(message(Message.STARTING_GAME).build());
                this.game.dealInitialHands();
                // Send each real player their cards.
                this.updateCards();
                // Send the first message to the player. Order will be a random of the real players, followed by AI, and then dealer.
                final Player nextPlayer = this.game.getNextPlayer();
                if (nextPlayer.isReal()) {
                    this.sendYourTurn(nextPlayer);
                } else {
                    throw new NotImplementedException("There should always be at least 1 human player to send to first.");
                }
                break;
            case "GAME_STAY":
            case "GAME_HIT":
            case "GAME_SPLIT":
                final GameOption option = GameOption.valueOf(contents[0].split("_")[1]);
                final Player player = this.game.getPlayerFor(session);
                LOG.info("{} has decided to {}.", this.game.getSessionIdFor(player), option);
                this.game.performOption(player, option);
                // Send to other than the player what their move was.
                this.broadCastMessage(session, message(Message.MOVE_MADE, session.getId(), option).build());
                this.updateCards();
                final Player next = this.game.getNextPlayer();
                if (next.isReal()) {
                    this.sendYourTurn(next);
                } else {
                    LOG.info("All real players have gone. Processing AI.");
                    this.broadCastMessageFromServer(message(Message.PROCESSING_AI).build());
                }
                break;
            default:
                break;
        }
    }

    /**
     * Update the cards on the client side.
     */
    private void updateCards() {
        // Send each real player their cards.
        final Map<Player, List<TextMessage>> cardMessages = this.game.buildHandMessages();
        cardMessages.forEach((player, messages) ->
                messages.forEach(toSend -> this.sendMessage(player.getSession(), toSend)));
    }

    /**
     * Send 'your turn' to next player.
     *
     * @param player the player.
     */
    private void sendYourTurn(final Player player) {
        LOG.info("Sending YOUR_TURN to {}", this.game.getSessionIdFor(player));
        this.sendMessage(player.getSession(), message(Message.YOUR_TURN).build());
    }

    /**
     * When we're ready to start - register the AI and send the messages.
     */
    private void doReadyToStart() {
        this.game.registerAI();
        this.acceptingConnections = false;
        LOG.info("Game is now ready to start - sending message!");
        final Player admin = this.game.getAdmin();
        this.sendMessage(admin.getSession(), message(Message.READY_TO_START).build());
        this.broadCastMessage(admin.getSession(),
                message(Message.OTHER_READY_TO_START, admin.getSession().getId()).build());
    }

    /**
     * Send a message to the given session.
     *
     * @param recipient the session.
     * @param message   the message.
     */
    private void sendMessage(final WebSocketSession recipient, final TextMessage message) {
        try {
            recipient.sendMessage(message);
        } catch (final IOException exception) {
            LOG.error("Error sending a message.", exception);
            this.closeSession(recipient, CloseStatus.PROTOCOL_ERROR);
        }
    }

    /**
     * Broadcast a message to the other users connected to this socket.
     *
     * @param sender  the sender.
     * @param message the message.
     */
    private void broadCastMessage(final WebSocketSession sender, final TextMessage message) {
        LOG.trace("SENDING {} TO {}.", message.getPayload(), this.game.getConnectedPlayerSessions());
        this.game.getConnectedRealPlayers().stream()
                .map(Player::getSession)
                .filter(session -> !session.getId().equals(sender.getId()))
                .forEach(session ->
                {
                    try {
                        session.sendMessage(message);
                    } catch (final Exception exception) {
                        this.closeSession(session, CloseStatus.PROTOCOL_ERROR);
                    }
                });
    }

    /**
     * Send a message to every real player connected.
     *
     * @param message the message.
     */
    private void broadCastMessageFromServer(final TextMessage message) {
        this.game.getConnectedRealPlayers().stream()
                .map(Player::getSession)
                .forEach(session ->
                {
                    try {
                        session.sendMessage(message);
                    } catch (final Exception exception) {
                        this.closeSession(session, CloseStatus.PROTOCOL_ERROR);
                    }
                });
    }

    /**
     * Close a session.
     *
     * @param session the session.
     * @param status  the reason why we're closing.
     */
    private void closeSession(final WebSocketSession session, final CloseStatus status) {
        try {
            session.close(status);
        } catch (final IOException exception) {
            LOG.error("Exception when trying to close session!", exception);
        }
    }

}
