package hu.elte.web.gaborkrisko.seleniumtest;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTestBase {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected long defaultTimeOutInSeconds = 10;
    protected TestConfig config;
    protected ElementLocators locators;

    @Before
    public void setup() throws MalformedURLException {
        this.config = new TestConfigFactory().createFromEnvironment();
        this.locators = new ElementLocators();
        ChromeOptions options = new ChromeOptions();
        this.driver = new RemoteWebDriver(new URL(config.getDriverURL()), options);
        this.driver.manage().window().maximize();
        this.wait = new WebDriverWait(driver, defaultTimeOutInSeconds);
    }

    @After
    public void close() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    protected WebElement waitAndReturnElement(By locator) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this.driver.findElement(locator);
    }
}
