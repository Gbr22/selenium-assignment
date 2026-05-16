package hu.elte.web.gaborkrisko.seleniumtest;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import static org.junit.Assert.assertEquals;

public class AuthTest extends SeleniumTestBase {
    private void submitLogin(String username, String password) {
        this.driver.get(config.getLoginURL());
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locators.getLoginFormBy()));
        final WebElement usernameEl = waitAndReturnElement(locators.getUsernameInputBy());
        final WebElement passwordEl = waitAndReturnElement(locators.getPasswordInputBy());
        usernameEl.sendKeys(username);
        passwordEl.sendKeys(password);
        final WebElement submitEl = waitAndReturnElement(locators.getLoginSubmitBy());
        submitEl.click();
    }

    @Test
    public void loginTest() {
        String username = config.getAdminUsername();
        String password = config.getAdminPassword();
        submitLogin(username, password);
        final WebElement usernameEl = waitAndReturnElement(locators.getUsernameDisplayBy());
        assertEquals(username, usernameEl.getText());
    }
}
