package selenium.page;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

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

    @FindBy(id = "stay")
    public WebElement stay;

    @FindBy(id = "hit")
    public WebElement hit;

    @FindBy(id = "split")
    public WebElement split;

    @FindBy(id = "consoleText")
    public WebElement consoleText;

    @FindBy(id = "playerHandCards")
    public WebElement playerCards;

    @FindBy(id = "dealerHandCards")
    public WebElement dealerCards;

    @FindBy(id = "otherHandCards1")
    public WebElement otherPlayer1Cards;

    @FindBy(id = "otherHandCards2")
    public WebElement otherPlayer2Cards;

    /**
     * Quick start the game by connecting, opening the lobby, and starting the game.
     */
    public void quickStart() {
        this.connect();
        this.numberPlayers.sendKeys("1");
        this.open.click();
        this.start.click();
    }

    public int countNumberOfCardsFor(final WebElement cardList) {
        return this.getAllCardsFor(cardList).size();
    }

    /**
     * Fetch all the inner nodes of the given web element.
     *
     * @param cardList the list.
     * @return the list of 'li' elements.
     */
    public List<WebElement> getAllCardsFor(final WebElement cardList) {
        return this.webDriver.findElements(By.xpath(String.format("//ul[@id='%s']/li", cardList.getAttribute("id"))));
    }

    /**
     * Return the player's UID if connected.
     *
     * @return the UID.
     */
    public String getPlayerUID() {
        if (!this.connect.isEnabled()) {
            final String consoleText = this.consoleText.getText();
            return consoleText.replace("Console (UID: ", "").replace(")", "").trim();
        }
        return null;
    }

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
