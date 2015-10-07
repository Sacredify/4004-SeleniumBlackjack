package ca.carleton.blackjack.controller.game.entity;

import org.springframework.web.socket.WebSocketSession;

/**
 * Represents a player.
 * <p/>
 * Created by Mike on 10/6/2015.
 */
public class Player {

    private final WebSocketSession session;

    private boolean isAdmin;

    public Player(final WebSocketSession session) {
        this.session = session;
    }

    public boolean isReal() {
        return this.session != null;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(final boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public WebSocketSession getSession() {
        return this.session;
    }
}
