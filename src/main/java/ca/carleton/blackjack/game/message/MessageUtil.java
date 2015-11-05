package ca.carleton.blackjack.game.message;

/**
 * Stores message strings to use across the application.
 * <p/>
 * Created by Mike on 10/7/2015.
 */
public class MessageUtil {

    public enum Message {

        PLAYER_CONNECTED("|CONNECTED|Successfully connected to the game with unique id %s."),
        OTHER_PLAYER_CONNECTED("|OTHER+CONNECTED|%s has connected to the game."),
        PLAYER_DISCONNECTED("|DISCONNECTED|Disconnected to the game with unique id %s."),
        OTHER_PLAYER_DISCONNECTED("|OTHER+DISCONNECTED|%s has disconnected from the game."),
        ADMIN_SET(
                "|ADMIN|You have been designated the admin for this game. Enter the number of players/AI and open the lobby."),
        NOT_ACCEPTING("|NOT+ACCEPTING|The game isn't accepting connections. You will be disconnected."),
        DEALING_CARDS("|DEALING+CARDS|Displaying cards for round Please wait until it is your turn."),
        ADD_PLAYER_CARD("|ADD+PLAYER+CARD|%s"),
        ADD_DEALER_CARD("|ADD+DEALER+CARD|%s"),
        // Send index 0 or 1 so we know which one to fill out, as well as their ID to set.
        ADD_OTHER_PLAYER_CARD("|ADD+OTHER+PLAYER+CARD|%s|%s|%s"),
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
