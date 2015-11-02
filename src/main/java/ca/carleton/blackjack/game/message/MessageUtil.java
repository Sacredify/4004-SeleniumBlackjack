package ca.carleton.blackjack.game.message;

/**
 * Stores message strings to use across the application.
 * <p/>
 * Created by Mike on 10/7/2015.
 */
public class MessageUtil {

    public static enum Message {

        PLAYER_CONNECTED("|CONNECTED|Successfully connected to the game with unique id %s."),
        OTHER_PLAYER_CONNECTED("|OTHER+CONNECTED|%s has connected to the game."),
        PLAYER_DISCONNECTED("|DISCONNECTED|Disconnected to the game with unique id %s."),
        OTHER_PLAYER_DISCONNECTED("|OTHER+DISCONNECTED|%s has disconnected from the game."),
        ADMIN_SET(
                "|ADMIN|You have been designated the admin for this game. Enter the number of players/AI and open the lobby."),
        NOT_ACCEPTING("|NOT+ACCEPTING|The game isn't accepting connections. You will be disconnected."),
        ADD_CARD("|ADD+CARD|%s"),
        READY_TO_START("|READY+TO+START|The game is now ready to begin. Press start when ready."),
        OTHER_READY_TO_START("|OTHER+READY+TO+START|The game is now ready to begin. Waiting on admin to start.");

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
