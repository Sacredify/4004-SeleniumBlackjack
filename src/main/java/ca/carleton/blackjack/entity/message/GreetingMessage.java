package ca.carleton.blackjack.entity.message;

/**
 * Represents a greeting message passed via JSON.
 *
 * Created by Mike on 10/6/2015.
 */
public class GreetingMessage {

    private final String content;

    public GreetingMessage(final String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
