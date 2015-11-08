package selenium;

import config.SeleniumTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import selenium.page.IndexPage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test the ability to connect and disconnect without getting any weird errors.
 * <p/>
 * Created by Mike on 11/8/2015.
 */
@SeleniumTest
public class ConnectionBasicsTest extends AbstractSeleniumTest {

    @Autowired
    private IndexPage indexPage;

    @Test
    public void canConnect() {
        this.indexPage.connect();
        assertThat(this.indexPage.hasText("Successfully connected to the game with unique"), is(true));
        assertThat(this.indexPage.hasText("You have been designated the admin for this game."), is(true));
        assertThat(this.indexPage.hasText("Connection closed"), is(true));
        this.indexPage.disconnect();
    }

    @Test
    public void canOpenLobby() {
        this.indexPage.connect();
        assertThat(this.waitForDisplayed(this.indexPage.numberPlayers).isEnabled(), is(true));
        this.indexPage.open.click();
        assertThat(this.indexPage.hasText("Opening the lobby with specified settings."), is(true));
        this.indexPage.disconnect();
    }

    @Test
    public void canStartGame() {
        this.indexPage.connect();
        this.waitForDisplayed(this.indexPage.numberPlayers).isEnabled();
        this.indexPage.numberPlayers.sendKeys("1");
        this.indexPage.open.click();
        assertThat(this.indexPage.hasText("The game is now ready to begin. Press start when ready."), is(true));
        this.indexPage.start.click();
        assertThat(this.indexPage.hasText("The game has started! Please wait for your turn."), is(true));
        this.indexPage.disconnect();
    }
}
