package cucumber.feature;

import ca.carleton.blackjack.BlackJackApplication;
import ca.carleton.blackjack.game.BlackJackGame;
import ca.carleton.blackjack.game.GameOption;
import ca.carleton.blackjack.game.entity.AIPlayer;
import ca.carleton.blackjack.game.entity.Player;
import ca.carleton.blackjack.game.entity.card.Card;
import ca.carleton.blackjack.game.entity.card.Rank;
import ca.carleton.blackjack.game.entity.card.Suit;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Step definitions for our AI logic testing - split hand.
 * <p/>
 * Created by Mike on 11/3/2015.
 */
@SpringApplicationConfiguration(classes = BlackJackApplication.class)
public class AIStepDefs {

    private final AIPlayer ai = new AIPlayer(null);

    private Player otherPlayer;

    private int numberOfCards;

    @Autowired
    private BlackJackGame blackJackGame;

    @Given(".+card in the AI's hand with the rank '(.+)' and suit '(.+)' and visibility '(.+)'")
    public void addCard(final Rank rank, final Suit suit, final boolean visibility) {
        this.ai.getHand().addCard(new Card(rank, suit, visibility));
    }

    @Given(".+player with two cards in their hand consisting of '(.+)' of '(.+)', visibility '(.+)' and '(.+)' of '(.+)', visibility '(.+)'")
    public void addPlayerWithCards(final Rank rank, final Suit suit, final boolean visibility,
                                   final Rank rank2, final Suit suit2, final boolean visibility2) {
        this.blackJackGame.registerPlayer(null);
        // HACK but we only add 1 other player in the rest so it should work.
        this.otherPlayer = this.blackJackGame.getConnectedPlayers().get(0);
        this.otherPlayer.getHand().addCard(new Card(rank, suit, visibility));
        this.otherPlayer.getHand().addCard(new Card(rank2, suit2, visibility2));
        this.otherPlayer.setLastOption(GameOption.STAY);
    }

    @When("^it is the AI's turn to make a move")
    public void prepareTurn() {
        this.numberOfCards = this.ai.getHand().getCards().size();
        ;
    }

    @Then("the AI should perform their turn")
    public void getOption() {
        this.blackJackGame.doAITurn(this.ai);
    }

    @Then("the AI's last move should be '(.+)'")
    public void verifyLastMove(final GameOption gameOption) {
        assertThat(this.ai.getLastOption(), is(gameOption));
    }

    @Then("the other player's last move should be '(.+)'")
    public void verifyLastMoveOtherPlayer(final GameOption gameOption) {
        assertThat(this.otherPlayer.getLastOption(), is(gameOption));
    }

    @Then("the AI's hand should have one more card than before")
    public void verifyHandSizeChanged() {
        assertThat(this.numberOfCards + 1, is(this.ai.getHand().getCards().size()));
    }

    @Then("the AI's hand size should remained unchanged")
    public void verifyHandSizeUnchanged() {
        assertThat(this.numberOfCards, is(this.ai.getHand().getCards().size()));
    }

}
