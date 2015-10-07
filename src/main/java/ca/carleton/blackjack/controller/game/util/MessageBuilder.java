package ca.carleton.blackjack.controller.game.util;

import org.springframework.web.socket.TextMessage;

/**
 * Helper to make a formatted message to send.
 * <p/>
 * Created by Mike on 10/7/2015.
 */
public class MessageBuilder {

    private String message;

    private String sender;

    MessageBuilder(final String message) {
        this.message = message;
    }

    public static MessageBuilder message(final String message) {
        return new MessageBuilder(message);
    }

    public MessageBuilder withSender(final String sender) {
        this.sender = sender;
        return this;
    }

    public MessageBuilder withFormat(final Object... arguments) {
        message = String.format(message, arguments);
        return this;
    }

    public TextMessage build() {
        return new TextMessage(String.format("<strong>%s:</strong> %s", this.sender, this.message));
    }

}
