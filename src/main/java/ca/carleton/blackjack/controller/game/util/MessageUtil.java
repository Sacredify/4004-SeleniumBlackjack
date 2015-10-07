package ca.carleton.blackjack.controller.game.util;

/**
 * Stores message strings to use across the application.
 * <p/>
 * Created by Mike on 10/7/2015.
 */
public class MessageUtil {

    public static enum Message {

        PLAYER_CONNECTED("_CONNECTED_Successfully connected to the game with unique id %s."),
        OTHER_PLAYER_CONNECTED("_OTHER+CONNECTED_%s has connected to the game."),
        PLAYER_DISCONNECTED("_DISCONNECTED_Disconnected to the game with unique id %s."),
        OTHER_PLAYER_DISCONNECTED("_OTHER+DISCONNECTED_%s has disconnected from the game."),
        ADMIN_SET("_ADMIN_You have been designated the admin for this game."),
        NOT_ACCEPTING("_NOT+ACCEPTING_The game isn't accepting connections. You will be disconnected.");

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
