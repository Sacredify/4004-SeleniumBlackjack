package page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Greeting page used by selenium to model a page.
 *
 * Created by Mike on 10/6/2015.
 */
public class EchoPage extends AbstractPage<EchoPage> {

    @FindBy(id = "connect")
    public WebElement connect;

    @FindBy(id = "disconnect")
    public WebElement disconnect;

    @FindBy(id = "message")
    public WebElement message;

}
