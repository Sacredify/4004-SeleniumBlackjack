package cucumber.feature;

import ca.carleton.blackjack.BlackJackApplication;
import ca.carleton.blackjack.game.BlackJackGame;
import ca.carleton.blackjack.game.entity.Player;
import ca.carleton.blackjack.game.entity.card.Card;
import ca.carleton.blackjack.game.entity.card.HandStatus;
import ca.carleton.blackjack.game.entity.card.Rank;
import ca.carleton.blackjack.game.entity.card.Suit;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.Is.is;

/**
 * Step definitions for win conditions.
 * <p/>
 * Created by Mike on 11/3/2015.
 */
@SpringApplicationConfiguration(classes = BlackJackApplication.class)
public class WinConditionStepDefs {

    final Player player = new Player(null);

    @Autowired
    private BlackJackGame blackJackGame;

    @Given(".+card in the player's hand with the rank '(.+)' and suit '(.+)' and hidden '(.+)'")
    public void addCard(final Rank rank, final Suit suit, final boolean hidden) {
        this.player.getHand().addCard(new Card(rank, suit, hidden));
    }

    @When("^the player draws his seventh card with the rank '(.+)' and suit '(.+)' and hidden '(.+)'")
    public void drawLastCard(final Rank rank, final Suit suit, final boolean hidden) throws Throwable {
        this.player.getHand().addCard(new Card(rank, suit, hidden));
        assertThat(this.player.getHand().getCards().size(), is(7));
    }

    @And("^the player's hand value doesn't exceed (\\d+)$")
    public void checkHandValue(final int handValue) throws Throwable {
        assertThat(this.player.getHand().getHandValue(), is(lessThan((long) handValue)));
    }

    @Then("^the player immediately wins with a seven card charlie")
    public void setWinner() throws Throwable {
        this.blackJackGame.registerPlayer(null);
        this.blackJackGame.resolveRoundSevenCardCharlie(this.player);
    }

    @And("^the other player's statuses should be set")
    public void checkLosers() throws Throwable {
        assertThat(this.blackJackGame.getConnectedPlayers()
                .stream()
                .filter(player -> player.getHand().getHandStatus() == HandStatus.LOSER)
                .count(), is(1L));
    }

    @And("^the player's status should be set")
    public void checkWinner() throws Throwable {
        assertThat(this.player.getHand().getHandStatus(), is(HandStatus.SEVEN_CARD_CHARLIE));
    }
}
