package hu.elte.web.gaborkrisko.seleniumtest;

import java.util.Map;

import org.openqa.selenium.By;

import lombok.Getter;

@Getter
public class ElementLocators {
    private final By usernameInputBy = By.id("username");
    private final By passwordInputBy = By.id("password");
    private final By loginFormBy = By.id("bank-login-content");
    private final By usernameDisplayBy = By.id("username-display");
    private final By loginSubmitBy = By.id("login-btn");
    private final By logoutBy = By.id("logout-btn");
    private final By addAccountBy = By.id("add-account-link");
    private final By accountFormBy = By.id("account-form");
    private final By accountNameInputBy = By.id("account-name");
    private final By accountTypeBy = By.id("account-type");
    private final By saveAccountBy = By.id("save-account-btn");
    private final By initialBalanceInputBy = By.id("initial-balance");
    private final By toastItemBy = By.xpath("//*[@data-sonner-toaster='true']//*[@data-sonner-toast]");
    private final By toastItemTitleBy = By.xpath("//*[@data-sonner-toaster='true']//*[@data-sonner-toast]//*[@data-content]//*[@data-title]");
    
    private final Map<Boolean, By> accountStatusRadioLabelsBy = Map.of(
        true, By.xpath("//*[@id='account-status-field']//*[@id='status-radio-group']//label[@for='status-active']"),
        false, By.xpath("//*[@id='account-status-field']//*[@id='status-radio-group']//label[@for='status-inactive']")
    );

    private final Map<Boolean, String> accountStatusRadioInputsXPathString = Map.of(
        true, "//*[@id='account-status-field']//*[@id='status-radio-group']//input[@type='radio' and @value='active']",
        false, "//*[@id='account-status-field']//*[@id='status-radio-group']//input[@type='radio' and @value='inactive']"
    );
    
    private final Map<AccountType, By> accountTypeSelectOptions = Map.of(
        AccountType.SAVINGS_ACCOUNT,  By.xpath("//*[@data-radix-select-viewport]//*[@data-radix-collection-item and contains(., 'Savings Account')]"),
        AccountType.CHECKING_ACCOUNT, By.xpath("//*[@data-radix-select-viewport]//*[@data-radix-collection-item and contains(., 'Checking Account')]"),
        AccountType.CREDIT_CARD,      By.xpath("//*[@data-radix-select-viewport]//*[@data-radix-collection-item and contains(., 'Credit Card')]")
    );
}
