package ca.carleton.blackjack.game;

import ca.carleton.blackjack.game.entity.card.Card;
import ca.carleton.blackjack.game.entity.card.Rank;
import ca.carleton.blackjack.game.entity.card.Suit;
import ca.carleton.blackjack.session.SessionHandler;
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
    private BlackJackService blackJackService;

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

                // TEST CARD
                final Card card = new Card(Rank.EIGHT, Suit.CLUBS);
                this.sendMessage(session, message(Message.ADD_CARD, card.toHTMLString()).build());

            }

            if (this.game.readyToStart()) {
                this.game.registerAI();
                // TODO start game!
                LOG.info("Starting game!");
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
        final String[] contents = message.getPayload().split("_");

        switch (contents[0]) {
            case "ACCEPT":
                LOG.info("Now accepting connections.");
                this.acceptingConnections = true;
                this.game.openLobby(Integer.parseInt(contents[1]));
                break;
            default:
                break;
        }
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
        LOG.info("SENDING {} TO {}.", message.getPayload(), this.game.getConnectedPlayers());
        this.game.getConnectedPlayers().stream()
                .filter(session -> session != null && !session.getId().equals(sender.getId()))
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

    private void disconnectUser(final WebSocketSession session) {
        final Runnable disconnect = () -> {
            try {
                Thread.sleep(2000);
                LOG.warn("Disconnecting {}.", session.getId());
            } catch (InterruptedException ignored) {
            }
            BlackJackSocketHandler.this.closeSession(session, CloseStatus.NOT_ACCEPTABLE);
        };
        new Thread(disconnect).run();
    }
}
