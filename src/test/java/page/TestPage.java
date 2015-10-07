package page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.context.annotation.Lazy;

/**
 * Page for test.hmtl
 *
 * Created by Mike on 10/6/2015.
 */
@Lazy
public class TestPage extends AbstractPage<TestPage> {

    @FindBy(id = "test")
    public WebElement test;

    @FindBy(id = "click")
    public WebElement clickMe;

    @Override
    protected String getPageName() {
        return "test.html";
    }
}
