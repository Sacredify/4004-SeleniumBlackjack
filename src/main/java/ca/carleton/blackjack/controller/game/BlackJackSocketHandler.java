package ca.carleton.blackjack.controller.game;

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

import static ca.carleton.blackjack.controller.game.util.MessageUtil.Message;
import static ca.carleton.blackjack.controller.game.util.MessageUtil.message;

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

    @PostConstruct
    public void init() {
        game.startNewRound(4);
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        LOG.info("Opened new session for {}.", session.getId());
        if (this.game.registerPlayer(session)) {
            sendMessage(session, message(Message.PLAYER_CONNECTED, session.getId()).build());
            broadCastMessage(session, message(Message.OTHER_PLAYER_CONNECTED, session.getId()).build());
        } else {
            // TODO send rejection
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        LOG.info("Closing session for {} with status {}.", session, status);
        if (this.game.deregisterPlayer(session)) {
            // TODO send confirmation.
        } else {
            // We're in an invalid state - how was this called?
            throw new IllegalStateException("Invalid state! Session should already be destroyed.");
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
        LOG.info(message.getPayload());
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
        this.game.getConnectedPlayers().stream()
                .filter(session -> !session.getId().equals(sender.getId()))
                .forEach(session ->
                {
                    try {
                        session.sendMessage(message);
                    } catch (final Exception exception) {
                        closeSession(session, CloseStatus.PROTOCOL_ERROR);
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
        if (this.game.deregisterPlayer(session)) {
            LOG.info("Successfully deregistered session {}.", session.getId());
        }
        try {
            session.close(status);
        } catch (final IOException exception) {
            LOG.error("Exception when trying to close session!", exception);
        }
    }
}
