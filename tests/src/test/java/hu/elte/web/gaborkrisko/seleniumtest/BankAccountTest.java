package hu.elte.web.gaborkrisko.seleniumtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BankAccountTest extends SeleniumTestBase {
    private final static String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully!";

    private static String formatBalance(long balanceCents) {
        long whole = balanceCents / 100;
        long cents = balanceCents - (whole * 100);
        String centsString = cents < 10 ? "0%d".formatted(cents) : "%d".formatted(cents);
        return "%d.%s".formatted(whole, centsString);
    }

    private static void fillAccountCreationForm(SeleniumTestBase test, AccountCreationParameters parameters) {
        final var locators = test.locators;
        final var addAccountEl = test.waitAndReturnElement(locators.getAddAccountBy());
        addAccountEl.sendKeys(Keys.ENTER);
        test.wait.until(ExpectedConditions.visibilityOfElementLocated(locators.getAccountFormBy()));
        
        final var accountNameEl = test.waitAndReturnElement(locators.getAccountNameInputBy());
        accountNameEl.sendKeys(parameters.getAccountName());
        
        final var accountTypeBy = test.waitAndReturnElement(locators.getAccountTypeBy());
        accountTypeBy.click();
        final var option = test.waitAndReturnElement(locators.getAccountTypeSelectOptions().get(parameters.getAccountType()));
        option.click();

        final var initialBalanceEl = test.waitAndReturnElement(locators.getInitialBalanceInputBy());
        initialBalanceEl.sendKeys(formatBalance(parameters.getBalanceCents()));

        final var isActiveRadioLabelToSelectBy = locators.getAccountStatusRadioLabelsBy().get(parameters.getIsActive());
        final var isActiveRadioInputToSelectXPath = locators.getAccountStatusRadioHiddenInputsXPathString().get(parameters.getIsActive());
        final var isActiveRadioToSelectEl = test.waitAndReturnElement(isActiveRadioLabelToSelectBy);
        isActiveRadioToSelectEl.click();
        final var isHiddenCheckboxInputSelected = test.getCheckedStateByXPath(isActiveRadioInputToSelectXPath);
        assertTrue(isHiddenCheckboxInputSelected);

        final var enableOverdraftProtectionEl = test.waitAndReturnElement(locators.getEnableOverdraftProtectionCheckbox());
        if (parameters.getIsOverdraftProtectionEnabled()) {
            enableOverdraftProtectionEl.click();
        }
        assertEquals(parameters.getIsOverdraftProtectionEnabled(), Boolean.parseBoolean(enableOverdraftProtectionEl.getAttribute("aria-checked")));
        assertEquals(parameters.getIsOverdraftProtectionEnabled(), test.getCheckedStateByXPath(locators.getEnableOverdraftProtectionHiddenInputXPathString()));
    }

    private static void submitAccountCreationForm(SeleniumTestBase test, AccountCreationParameters parameters) {
        BankAccountTest.fillAccountCreationForm(test, parameters);
        test.waitAndReturnElement(test.locators.getSaveAccountBy()).click();
        assertTrue(ToastTest.getToastText(test).contains(ACCOUNT_CREATION_SUCCESS_MESSAGE), "Expected the toast message text to contain the account creation success message.");
    }

    public static void createAccount(SeleniumTestBase test, AccountCreationParameters parameters) {
        test.waitAndReturnElement(test.locators.getDashboardNavigationBy()).click();
        BankAccountTest.submitAccountCreationForm(test, parameters);
    }

    @Test
    public void createAccountTest() {
        AuthTest.loginAdmin(this);
        waitAndReturnElement(locators.getDashboardNavigationBy()).click();
        BankAccountTest.submitAccountCreationForm(this, AccountCreationParameters.builder()
            .accountName("via opener button")
            .accountType(AccountType.CREDIT_CARD)
            .balanceCents(1234L)
            .isActive(false)
            .isOverdraftProtectionEnabled(true)
            .build());
    }

    @Test
    public void openAccountCreationDialogWithKeyboardTest() {
        AuthTest.loginAdmin(this);
        dispatchKeyboardEvent("keydown", "N");
        BankAccountTest.submitAccountCreationForm(this, AccountCreationParameters.builder()
            .accountName("via keyboard")
            .accountType(AccountType.SAVINGS_ACCOUNT)
            .balanceCents(1234L)
            .isActive(true)
            .isOverdraftProtectionEnabled(false)
            .build());
    }
}
