package hu.elte.web.gaborkrisko.seleniumtest;

import org.openqa.selenium.By;

import lombok.Getter;

@Getter
public class ElementLocators {
    private final By usernameInputBy = By.id("username");
    private final By passwordInputBy = By.id("password");
    private final By loginFormBy = By.id("bank-login-content");
    private final By usernameDisplayBy = By.id("username-display");
    private final By loginSubmitBy = By.id("login-btn");
}
