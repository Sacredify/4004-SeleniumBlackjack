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

import javax.annotation.PostConstruct;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Step definitions for the dealer when he hits because his hand is less than 17.
 * <p/>
 * Created by Mike on 11/3/2015.
 */
@SpringApplicationConfiguration(classes = BlackJackApplication.class)
public class DealerHitStepDefs {

    private AIPlayer dealer;

    private int numberOfCards;

    @Autowired
    private BlackJackGame blackJackGame;

    @PostConstruct
    public void init() {
        this.dealer = new AIPlayer(null);
        this.dealer.setDealer(true);
    }

    @Given(".+card in the dealer's hand with the rank '(.+)' and suit '(.+)' and visibility '(.+)'")
    public void addCard(final Rank rank, final Suit suit, final boolean visibility) {
        this.dealer.getHand().addCard(new Card(rank, suit, visibility));
    }

    @When("^it is the dealer's turn to make a move")
    public void prepareTurn() {
        this.numberOfCards = this.dealer.getHand().getCards().size();
    }

    @Then("the dealer should hit")
    public void getOption() {
        this.blackJackGame.doAITurn(this.dealer);
    }

    @Then("the dealer's last move should be '(.+)'")
    public void verifyHit(final GameOption gameOption) {
        assertThat(this.dealer.getLastOption(), is(gameOption));
    }

    @Then("the dealer's hand should have one more card than before")
    public void verifyHandSize() {
        assertThat(this.dealer.getHand().getCards().size(), is(this.numberOfCards + 1));
    }

}
