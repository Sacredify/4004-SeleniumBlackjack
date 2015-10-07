package ca.carleton.blackjack.controller.game.util;

/**
 * Stores message strings to use across the application.
 * <p/>
 * Created by Mike on 10/7/2015.
 */
public class MessageUtil {

    public static enum Message {

        PLAYER_CONNECTED("Successfully connected to the game with unique id %s."),
        OTHER_PLAYER_CONNECTED("%s has connected to the game."),
        PLAYER_DISCONNECTED("Disconnected to the game with unique id %s."),
        OTHER_PLAYER_DISCONNECTED("%s has disconnected from the game.");

        private final String content;

        Message(final String content) {
            this.content = content;
        }

        public String getContent() {
            return this.content;
        }
    }

    private static final String SERVER_UID = "Server";

    public static MessageBuilder message(final Message message, final Object... formatArgs) {
        return new MessageBuilder(message.getContent())
                .withFormat(formatArgs)
                .withSender(SERVER_UID);
    }
}
