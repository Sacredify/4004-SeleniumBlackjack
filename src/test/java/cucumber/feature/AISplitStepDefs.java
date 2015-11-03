package cucumber.feature;

import ca.carleton.blackjack.BlackJackApplication;
import ca.carleton.blackjack.game.BlackJackService;
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

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Step definitions for our AI logic testing - split hand.
 * <p/>
 * Created by Mike on 11/3/2015.
 */
@SpringApplicationConfiguration(classes = BlackJackApplication.class)
public class AISplitStepDefs {

    private final AIPlayer ai = new AIPlayer(null);

    private GameOption chosenOption;

    @Autowired
    private BlackJackService blackJackService;

    @Given(".+card with the rank '(.+)' and visibility '(.+)'")
    public void addCard(final Rank rank, final boolean visibility) {
        this.ai.getHand().addCard(new Card(rank, Suit.DIAMONDS, visibility));
    }

    @When("^it is the AI's turn to make a move")
    public void prepareTurn() {
        // Nothing to do - this is a networking portion.
    }

    @Then("the AI should split their hand")
    public void getOption() {
        this.chosenOption = this.blackJackService.getAIOption(this.ai, emptyList());
        this.ai.setLastOption(this.chosenOption);
    }

    @Then("the AI's last move should be '(.+)'")
    public void verify(final GameOption gameOption) {
        assertThat(this.chosenOption, is(gameOption));
        assertThat(this.ai.getLastOption(), is(gameOption));
    }

}
