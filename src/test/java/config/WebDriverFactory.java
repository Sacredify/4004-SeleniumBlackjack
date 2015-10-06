package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

/**
 * Web ca.carleton.blackjack.driver factory.
 *
 * Created by Mike on 10/6/2015.
 */
public class WebDriverFactory {

    public WebDriverFactory() {}

    public WebDriver getWebDriver() {
        final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setJavascriptEnabled(true);

        final WebDriver fireFoxDriver = new FirefoxDriver(capabilities);
        fireFoxDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        return fireFoxDriver;
    }
}
