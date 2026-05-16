import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.URL;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;

public class AuthTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private long defaultTimeOutInSeconds = 10;
    private final String BASE_URL = System.getenv("TEST_BASE_URL");
    private final String DRIVER_URL = System.getenv("TEST_DRIVER_URL");
    private final String LOGIN_URL = BASE_URL;
    private final String USERNAME = System.getenv("TEST_ADMIN_USERNAME");
    private final String PASSWORD = System.getenv("TEST_ADMIN_PASSWORD");
    private final By usernameBy = By.id("username");
    private final By passwordBy = By.id("password");
    private final By loginFormBy = By.id("bank-login-content");
    private final By usernameDisplayBy = By.id("username-display");
    private final By loginSubmitBy = By.id("login-btn");

    @Before
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        this.driver = new RemoteWebDriver(new URL(DRIVER_URL), options);
        this.driver.manage().window().maximize();
        this.wait = new WebDriverWait(driver, defaultTimeOutInSeconds);
    }

    @After
    public void close() {
        if (this.driver != null) {
            this.driver.quit();
        }
    }

    private WebElement waitAndReturnElement(By locator) {
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return this.driver.findElement(locator);
    }

    private void submitLogin(String username, String password) {
        this.driver.get(LOGIN_URL);
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(loginFormBy));
        final WebElement usernameEl = waitAndReturnElement(usernameBy);
        final WebElement passwordEl = waitAndReturnElement(passwordBy);
        usernameEl.sendKeys(username);
        passwordEl.sendKeys(password);
        final WebElement submitEl = waitAndReturnElement(loginSubmitBy);
        submitEl.click();
    }

    @Test
    public void loginTest() {
        submitLogin(USERNAME, PASSWORD);
        final WebElement usernameEl = waitAndReturnElement(usernameDisplayBy);
        assertEquals(USERNAME, usernameEl.getText());
    }
}
