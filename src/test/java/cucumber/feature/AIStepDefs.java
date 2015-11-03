package cucumber.feature;

import ca.carleton.blackjack.BlackJackApplication;
import ca.carleton.blackjack.game.BlackJackGame;
import ca.carleton.blackjack.game.GameOption;
import ca.carleton.blackjack.game.entity.AIPlayer;
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

    private int numberOfCards;

    @Autowired
    private BlackJackGame blackJackGame;

    @Given(".+card in the AI's hand with the rank '(.+)' and suit '(.+)' and visibility '(.+)'")
    public void addCard(final Rank rank, final Suit suit, final boolean visibility) {
        this.ai.getHand().addCard(new Card(rank, suit, visibility));
    }

    @When("^it is the AI's turn to make a move")
    public void prepareTurn() {
        this.numberOfCards = this.ai.getHand().getCards().size();
    }

    @Then("the AI should split their hand")
    public void getOption() {
        this.blackJackGame.doAITurn(this.ai);
    }

    @Then("the AI's last move should be '(.+)'")
    public void verify(final GameOption gameOption) {
        assertThat(this.ai.getLastOption(), is(gameOption));
    }

    @Then("the AI's hand size should remained unchanged")
    public void verifyHandSize() {
        assertThat(this.numberOfCards, is(this.ai.getHand().getCards().size()));
    }

}
