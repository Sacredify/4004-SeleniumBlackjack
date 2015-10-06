package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Represents a page.
 *
 * Created by Mike on 10/6/2015.
 */
@Component
public abstract class AbstractPage<T extends AbstractPage<T>> {

    @Autowired
    protected WebDriver webDriver;

    @PostConstruct
    public void init() {
        PageFactory.initElements(this.webDriver, this);
    }

    public AbstractPage() {
        super();
    }

    public AbstractPage(final WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(this.webDriver, this);
    }

    public String getTitle() {
        return this.webDriver.getTitle();
    }

    public String getUrl() {
        return this.webDriver.getCurrentUrl();
    }
}
