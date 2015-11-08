package selenium;

import config.SeleniumTest;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import selenium.page.IndexPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Test our game options.
 * <p/>
 * Created by Mike on 11/8/2015.
 */
@SeleniumTest
public class GameOptionsTest extends AbstractSeleniumTest {

    @Autowired
    private IndexPage indexPage;

    @Test
    public void canUseHitOption() {
        this.indexPage.quickStart();
        assertThat(this.waitForDisplayed(this.indexPage.hit).isEnabled(), is(true));
        this.indexPage.hit.click();
        assertThat(this.indexPage.hasText("You decided to HIT. Sending to server - please wait for your next turn."),
                is(true));
        this.indexPage.disconnect();
    }

    @Test
    public void canUseStayOption() {
        this.indexPage.quickStart();
        assertThat(this.waitForDisplayed(this.indexPage.stay).isEnabled(), is(true));
        this.indexPage.stay.click();
        assertThat(this.indexPage.hasText("You decided to STAY. Sending to server - please wait for your next turn."),
                is(true));
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
}
