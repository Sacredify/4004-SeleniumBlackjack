package ca.carleton.blackjack.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Socket handler that will contain our blackjack game logic.
 * <p/>
 * Created by Mike on 10/6/2015.
 */
@Component
public class BlackJackSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage("Hello!"));
    }

}
