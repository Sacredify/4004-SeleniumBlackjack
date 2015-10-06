package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.test.context.TestExecutionListeners;

import java.lang.annotation.*;

/**
 * Custom class taken from:
 * <p/>
 * http://www.javacodegeeks.com/2015/03/spring-boot-integration-testing-with-selenium.html
 * <p/>
 * Created by Mike on 10/6/2015.
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestExecutionListeners(listeners = SeleniumTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public @interface SeleniumTest {

    Class<? extends WebDriver> driver() default FirefoxDriver.class;

    String baseUrl() default "http://localhost:8080";
}
