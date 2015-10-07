package page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 * Greeting page used by selenium to model a page.
 *
 * Created by Mike on 10/6/2015.
 */
@Lazy
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
        return testPage;
    }
}
