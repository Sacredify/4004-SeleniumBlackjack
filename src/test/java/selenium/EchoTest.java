package selenium;

import config.SeleniumTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import selenium.page.EchoPage;
import selenium.page.TestPage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for spring greeting controller.
 * <p/>
 * Created by Mike on 10/6/2015.
 */
@SeleniumTest(baseUrl = "http://localhost:8080/")
public class EchoTest extends AbstractSeleniumTest {

    @Autowired
    private EchoPage echoPage;

    @Test
    public void canConnectToBackend() throws Exception {
        this.echoPage.connect.click();
        assertThat(this.waitForDisplayed(this.echoPage.message).isDisplayed(), is(true));
    }

    @Test
    public void canDisconnectFromBackend() throws Exception {
        this.echoPage.connect.click();
        this.waitForDisplayed(this.echoPage.message);
        this.echoPage.disconnect.click();
        this.waitForHidden(this.echoPage.message);
        assertThat(this.echoPage.message.isDisplayed(), is(false));
    }

    @Test
    public void canSwitchPagesToTest() throws Exception {
        final TestPage testPage = this.echoPage.toTestPage();
        assertThat(testPage.test.isDisplayed(), is(false));
        testPage.clickMe.click();
        assertThat(this.waitForDisplayed(testPage.test).isDisplayed(), is(true));
    }
}
