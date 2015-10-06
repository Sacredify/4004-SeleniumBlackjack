import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import page.GreetingPage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for spring greeting controller.
 * <p/>
 * Created by Mike on 10/6/2015.
 */
public class GreetingControllerTest extends AbstractSeleniumTest {

    @Autowired
    private GreetingPage greetingPage;

    @Test
    public void canConnectToBackend() throws Exception {
        this.greetingPage.connect.click();
        assertThat(this.waitForDisplayed(this.greetingPage.name).isDisplayed(), is(true));
    }

    @Test
    public void canDisconnectFromBackend() throws Exception {
        this.greetingPage.connect.click();
        this.waitForDisplayed(this.greetingPage.name);
        this.greetingPage.disconnect.click();
        assertThat(this.greetingPage.name.isDisplayed(), is(false));
    }
}
