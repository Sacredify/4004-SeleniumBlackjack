package page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a page.
 *
 * Created by Mike on 10/6/2015.
 */
@Component
@Lazy
public abstract class AbstractPage<T extends AbstractPage<T>> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractPage.class);

    @Autowired
    protected WebDriver webDriver;

    protected abstract String getPageName();

    @PostConstruct
    public void init() {
        LOG.warn("Initializing elements for page {}.", getClass());
        PageFactory.initElements(this.webDriver, this);
    }

    public AbstractPage() {
        super();
    }

    public AbstractPage(final WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(this.webDriver, this);
    }

    public T open() {
        final Map<String, Object> parameters = new HashMap<>();
        return this.openPage(parameters);
    }

    private T openPage(final Map<String, Object> parameters) {
        this.webDriver.get(String.format("http://localhost:8080/%s", this.getPageName()));
        return (T) this;
    }

    public String getTitle() {
        return this.webDriver.getTitle();
    }

    public String getUrl() {
        return this.webDriver.getCurrentUrl();
    }
}

