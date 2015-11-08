package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Mock a user (aka a second browser).
 * <p/>
 * Created by Mike on 11/8/2015.
 */
@Service
public class MockUserFactory {

    public WebDriver getSecondUser(final String location) {
        return this.buildNewUser(location);
    }

    private WebDriver buildNewUser(final String location) {
        final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
        capabilities.setJavascriptEnabled(true);

        final WebDriver fireFoxDriver = new FirefoxDriver(capabilities);
        fireFoxDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        fireFoxDriver.get(location);
        return fireFoxDriver;
    }
}
