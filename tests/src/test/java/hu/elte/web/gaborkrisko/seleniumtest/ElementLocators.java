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
    private final By dashboardNavigationBy = By.id("nav-dashboard");
    private final By accountsNavigationBy = By.id("nav-accounts");
    private final By transactionsNavigationBy = By.id("nav-transactions");
    private final String pinnedAccountsXPathString = "//*[@id='pinned-accounts-drop-zone']";
    private final String pinnedAccountNameXPathString = "//*[@id='pinned-accounts-drop-zone']//*[@data-account-id]";
    private final By toastItemBy = By.xpath("//*[@data-sonner-toaster='true']//*[@data-sonner-toast]");
    private final By toastItemTitleBy = By.xpath("//*[@data-sonner-toaster='true']//*[@data-sonner-toast]//*[@data-content]//*[@data-title]");
    private final By enableOverdraftProtectionCheckbox = By.xpath("//*[@id='account-form']//*[@id='account-form-fields']//*[@data-testid='overdraft-checkbox' and @role='checkbox']");
    private final String enableOverdraftProtectionHiddenInputXPathString = "//*[@id='account-form']//*[@id='account-form-fields']//input[@type='checkbox' and @name='enableOverdraft']";
    
    private final Map<Boolean, By> accountStatusRadioLabelsBy = Map.of(
        true, By.xpath("//*[@id='account-status-field']//*[@id='status-radio-group']//label[@for='status-active']"),
        false, By.xpath("//*[@id='account-status-field']//*[@id='status-radio-group']//label[@for='status-inactive']")
    );

    private final Map<Boolean, String> accountStatusRadioHiddenInputsXPathString = Map.of(
        true, "//*[@id='account-status-field']//*[@id='status-radio-group']//input[@type='radio' and @value='active']",
        false, "//*[@id='account-status-field']//*[@id='status-radio-group']//input[@type='radio' and @value='inactive']"
    );
    
    private final Map<AccountType, By> accountTypeSelectOptions = Map.of(
        AccountType.SAVINGS_ACCOUNT,  By.xpath("//*[@data-radix-select-viewport]//*[@data-radix-collection-item and contains(., 'Savings Account')]"),
        AccountType.CHECKING_ACCOUNT, By.xpath("//*[@data-radix-select-viewport]//*[@data-radix-collection-item and contains(., 'Checking Account')]"),
        AccountType.CREDIT_CARD,      By.xpath("//*[@data-radix-select-viewport]//*[@data-radix-collection-item and contains(., 'Credit Card')]")
    );
}
