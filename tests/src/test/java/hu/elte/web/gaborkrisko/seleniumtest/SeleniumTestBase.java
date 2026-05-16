package hu.elte.web.gaborkrisko.seleniumtest;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.gson.Gson;

@TestInstance(Lifecycle.PER_CLASS)
public class SeleniumTestBase {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Duration defaultTimeout = Duration.ofSeconds(10);
    protected TestConfig config;
    protected ElementLocators locators;
    protected JavascriptExecutor javascriptExecutor;

    @BeforeAll
    public void setup() throws MalformedURLException {
        this.config = new TestConfigFactory().createFromEnvironment();
        this.locators = new ElementLocators();
        ChromeOptions options = new ChromeOptions();
        final var driver = new RemoteWebDriver(new URL(config.getDriverURL()), options);
        this.javascriptExecutor = (JavascriptExecutor) driver;
        this.driver = driver;
        this.driver.manage().window().maximize();
        this.wait = new WebDriverWait(driver, defaultTimeout);
        
    }

    @BeforeEach
    public void clearStorage() {
        driver.get(config.getBaseURL());
        javascriptExecutor.executeScript("window.localStorage.clear()");
        javascriptExecutor.executeScript("window.sessionStorage.clear()");
        driver.get(config.getBaseURL());
    }

    @AfterAll
    public void close() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    protected WebElement waitAndReturnElement(By locator) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this.driver.findElement(locator);
    }

    protected List<WebElement> waitAndReturnElements(By locator) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this.driver.findElements(locator);
    }

    private String asJsonString(String input) {
        Gson gson = new Gson();
        return gson.toJson(input, String.class);
    }

    private String getEvaluateXPathJsCode(String xpath) {
        return "document.evaluate(%s, document.documentElement, null, XPathResult.ANY_TYPE, null)".formatted(asJsonString(xpath));
    }
    private String getXPathFirstElementJsCode(String xpath) {
        return "(%s)?.iterateNext()".formatted(getEvaluateXPathJsCode(xpath));
    }

    protected boolean getCheckedStateByXPath(String xpath) {
        return (boolean) javascriptExecutor.executeScript(
            "return Boolean((%s)?.checked)"
            .formatted(getXPathFirstElementJsCode(xpath))
        );
    }

    protected void clickElementByXPath(String xpath) {
        javascriptExecutor.executeScript(
            "(%s)?.click()"
            .formatted(getXPathFirstElementJsCode(xpath))
        );
    }

    protected void dispatchKeyboardEvent(String eventType, String key) {
        javascriptExecutor.executeScript(
            "window.dispatchEvent(new KeyboardEvent(%s, {key: %s}))"
            .formatted(asJsonString(eventType), asJsonString(key))
        );
    }
}
