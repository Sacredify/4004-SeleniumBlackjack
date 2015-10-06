import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import page.EchoPage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test for spring greeting controller.
 * <p/>
 * Created by Mike on 10/6/2015.
 */
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
}
