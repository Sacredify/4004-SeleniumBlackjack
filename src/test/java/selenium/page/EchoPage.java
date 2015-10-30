package selenium.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Greeting selenium.page used by selenium to model a selenium.page.
 *
 * Created by Mike on 10/6/2015.
 */
@Lazy
@Component
public class EchoPage extends AbstractPage<EchoPage> {

    @FindBy(id = "connect")
    public WebElement connect;

    @FindBy(id = "disconnect")
    public WebElement disconnect;

    @FindBy(id = "message")
    public WebElement message;

    @FindBy(id = "testGo")
    public WebElement testLink;

    @Autowired
    private TestPage testPage;

    @Override
    protected String getPageName() {
        return "";
    }

    public TestPage toTestPage() {
        this.testLink.click();
        return this.testPage;
    }
}
