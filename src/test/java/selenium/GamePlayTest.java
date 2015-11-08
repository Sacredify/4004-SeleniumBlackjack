package selenium;

import config.SeleniumTest;
import org.apache.commons.lang3.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import selenium.page.IndexPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test our game options - whether we get a card, etc.
 * <p/>
 * Created by Mike on 11/8/2015.
 */
@SeleniumTest
public class GamePlayTest extends AbstractSeleniumTest {

    @Autowired
    private IndexPage indexPage;

    @Test
    public void canUseHitOption() {
        this.indexPage.quickStart();
        assertThat(this.waitForDisplayed(this.indexPage.hit).isEnabled(), is(true));
        final int numberCards = this.indexPage.countNumberOfCardsFor(this.indexPage.playerCards);
        this.indexPage.hit.click();
        assertThat(this.indexPage.hasText("You decided to HIT. Sending to server - please wait for your next turn."),
                is(true));
        assertThat(this.indexPage.countNumberOfCardsFor(this.indexPage.playerCards), is(numberCards + 1));
        this.indexPage.disconnect();
    }

    @Test
    public void canUseStayOption() {
        this.indexPage.quickStart();
        assertThat(this.waitForDisplayed(this.indexPage.stay).isEnabled(), is(true));
        final int numberCards = this.indexPage.countNumberOfCardsFor(this.indexPage.playerCards);
        this.indexPage.stay.click();
        assertThat(this.indexPage.hasText("You decided to STAY. Sending to server - please wait for your next turn."),
                is(true));
        assertThat(this.indexPage.countNumberOfCardsFor(this.indexPage.playerCards), is(numberCards));
        this.indexPage.disconnect();
    }

    @Test
    @Ignore
    public void canUseSplitOption() {
        this.indexPage.quickStart();
        assertThat(this.waitForDisplayed(this.indexPage.split).isEnabled(), is(true));
        this.indexPage.split.click();
        assertThat(this.indexPage.hasText("You decided to SPLIT. Sending to server - please wait for your next turn."),
                is(true));
        this.indexPage.disconnect();
    }

    @Test
    public void canPlayFullRound() {
        this.indexPage.quickStart();
        assertThat(this.indexPage.start.isEnabled(), is(false));
        this.indexPage.stay.click();
        // By now the AI should've done everything - start should be re-enabled.
        assertThat(this.indexPage.start.isEnabled(), is(true));
        assertThat(this.indexPage.hasText("To start another round, press the start button."), is(true));
        this.indexPage.disconnect();
    }

    @Test
    public void canPlayMultipleRounds() {
        this.indexPage.quickStart();
        assertThat(this.indexPage.start.isEnabled(), is(false));
        // Two quick games - stay each time to make the AI run through
        this.indexPage.stay.click();
        this.delay(3);
        this.indexPage.start.click();
        this.delay(3);
        this.indexPage.stay.click();
        this.delay(3);
        // Check to see if we have two game resolutions.
        final String output = this.indexPage.console.getAttribute("innerHTML");
        assertThat(StringUtils.countMatches(output, "To start another round, press the start button."), is(2));
        this.indexPage.disconnect();
    }
}
