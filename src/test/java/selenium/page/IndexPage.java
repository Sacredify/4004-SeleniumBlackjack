package selenium.page;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * The page for the main game.
 * <p/>
 * Created by Mike on 11/8/2015.
 */
@Lazy
@Component
public class IndexPage extends AbstractPage<IndexPage> {

    @FindBy(id = "connect")
    public WebElement connect;

    @FindBy(id = "disconnect")
    public WebElement disconnect;

    @FindBy(id = "numberPlayers")
    public WebElement numberPlayers;

    @FindBy(id = "open")
    public WebElement open;

    @FindBy(id = "start")
    public WebElement start;

    /**
     * Connect to the game.
     */
    public void connect() {
        this.connect.click();
    }

    /**
     * Disconnect from the game.
     */
    public void disconnect() {
        this.disconnect.click();
    }

    @Override
    protected String getPageName() {
        return StringUtils.EMPTY;
    }
}
