package selenium;

import config.SeleniumTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import selenium.page.IndexPage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests surrounding multiple uesrs.
 * <p/>
 * Created by Mike on 11/8/2015.
 */
@SeleniumTest
public class MultiPlayerTest extends AbstractSeleniumTest {

    @Autowired
    private IndexPage indexPage;

    @Test
    public void canMultiplePeopleConnect() {
        this.indexPage.connect();
        this.indexPage.setNumberPlayers(2);
        this.indexPage.open.click();
        // False, we want to wait for a second user.
        assertThat(this.indexPage.start.isEnabled(), is(false));

        // Lets connect a second player
        final WebDriver second = this.quickConnectSecondUser();
        this.delay(3);
        assertThat(this.indexPage.hasText("The game is now ready to begin. Press start when ready."), is(true));
        this.indexPage.disconnect();
        this.disconnectSecondUser(second);
    }

    @Test
    public void canMultiplePeoplePlayARound() {
        this.indexPage.connect();
        this.indexPage.setNumberPlayers(2);
        this.indexPage.open.click();
        final WebDriver second = this.quickConnectSecondUser();
        this.delay(3);
        // We're now ready to play
        this.indexPage.start.click();
        // Admin should always go first.
        this.indexPage.stay.click();
        second.findElement(By.id("stay")).click();
        this.delay(3);
        // Now we should be resolved.
        assertThat(this.indexPage.hasText("To start another round, press the start button."), is(true));
        this.disconnectSecondUser(second);
        this.indexPage.disconnect();
    }
}
