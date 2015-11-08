package selenium;

import ca.carleton.blackjack.BlackJackApplication;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.openqa.selenium.support.ui.ExpectedConditions.not;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

/**
 * Parent test for all selenium classes so we can wait for links.
 * <p/>
 * Created by Mike on 10/6/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = BlackJackApplication.class, locations = {"/META-INF/applicationContext.xml"})
@WebIntegrationTest(value = "server.port:8080")
public abstract class AbstractSeleniumTest {

    @Autowired
    protected WebDriver webDriver;

    /**
     * Wait for an element to become displayed (max 3 seconds).
     *
     * @param id the element's id.
     * @return the element.
     */
    public WebElement waitForDisplayed(final String id) {
        return new WebDriverWait(this.webDriver, 3).until(visibilityOf(this.webDriver.findElement(By.id(id))));
    }

    /**
     * Wait for an element to become displayed (max 3 seconds).
     *
     * @param element the element
     * @return the element.
     */
    public WebElement waitForDisplayed(final WebElement element) {
        return new WebDriverWait(this.webDriver, 3).until(visibilityOf(element));
    }

    /**
     * Wait for an element to become hidden.
     *
     * @param element the element
     */
    public void waitForHidden(final WebElement element) {
        new WebDriverWait(this.webDriver, 3).until(not(visibilityOf(element)));
    }
}
