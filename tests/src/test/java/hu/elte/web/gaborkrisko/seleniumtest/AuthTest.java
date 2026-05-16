package hu.elte.web.gaborkrisko.seleniumtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

public class AuthTest extends SeleniumTestBase {
    private final String LOGOUT_ALERT_MESSAGE = "Are you sure you want to logout?";

    public static void submitLogin(SeleniumTestBase test, String username, String password) {
        test.driver.get(test.config.getLoginURL());
        test.wait.until(ExpectedConditions.visibilityOfElementLocated(test.locators.getLoginFormBy()));
        final WebElement usernameEl = test.waitAndReturnElement(test.locators.getUsernameInputBy());
        final WebElement passwordEl = test.waitAndReturnElement(test.locators.getPasswordInputBy());
        usernameEl.sendKeys(username);
        passwordEl.sendKeys(password);
        final WebElement submitEl = test.waitAndReturnElement(test.locators.getLoginSubmitBy());
        submitEl.click();
    }
    public static void loginAdmin(SeleniumTestBase test) {
        String username = test.config.getAdminUsername();
        String password = test.config.getAdminPassword();
        submitLogin(test, username, password);
        final WebElement usernameEl = test.waitAndReturnElement(test.locators.getUsernameDisplayBy());
        assertEquals(username, usernameEl.getText());
    }

    @Test
    public void loginTest() {
        loginAdmin(this);
    }

    @Test
    public void logoutTest() {
        loginAdmin(this);
        final WebElement logoutEl = waitAndReturnElement(locators.getLogoutBy());
        logoutEl.click();
        final var alert = driver.switchTo().alert();
        assertEquals(LOGOUT_ALERT_MESSAGE, alert.getText());
        alert.accept();
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.getLoginFormBy()));
    }
}
