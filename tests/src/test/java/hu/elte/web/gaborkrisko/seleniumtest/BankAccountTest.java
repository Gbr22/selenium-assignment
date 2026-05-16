package hu.elte.web.gaborkrisko.seleniumtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BankAccountTest extends SeleniumTestBase {
    private final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully!";

    private String formatBalance(long balanceCents) {
        long whole = balanceCents / 100;
        long cents = balanceCents - (whole * 100);
        String centsString = cents < 10 ? "0%d".formatted(cents) : "%d".formatted(cents);
        return "%d.%s".formatted(whole, centsString);
    }

    private void fillAccountCreationForm(AccountCreationParameters parameters) {
        final var addAccountEl = waitAndReturnElement(locators.getAddAccountBy());
        addAccountEl.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.getAccountFormBy()));
        
        final var accountNameEl = waitAndReturnElement(locators.getAccountNameInputBy());
        accountNameEl.sendKeys(parameters.getAccountName());
        
        final var accountTypeBy = waitAndReturnElement(locators.getAccountTypeBy());
        accountTypeBy.click();
        final var option = waitAndReturnElement(locators.getAccountTypeSelectOptions().get(parameters.getAccountType()));
        option.click();

        final var initialBalanceEl = waitAndReturnElement(locators.getInitialBalanceInputBy());
        initialBalanceEl.sendKeys(formatBalance(parameters.getBalanceCents()));

        final var isActiveRadioLabelToSelectBy = locators.getAccountStatusRadioLabelsBy().get(parameters.getIsActive());
        final var isActiveRadioInputToSelectXPath = locators.getAccountStatusRadioHiddenInputsXPathString().get(parameters.getIsActive());
        final var isActiveRadioToSelectEl = waitAndReturnElement(isActiveRadioLabelToSelectBy);
        isActiveRadioToSelectEl.click();
        final var isHiddenCheckboxInputSelected = getCheckedStateByXPath(isActiveRadioInputToSelectXPath);
        assertTrue(isHiddenCheckboxInputSelected);

        final var enableOverdraftProtectionEl = waitAndReturnElement(locators.getEnableOverdraftProtectionCheckbox());
        if (parameters.getIsOverdraftProtectionEnabled()) {
            enableOverdraftProtectionEl.click();
        }
        assertEquals(parameters.getIsOverdraftProtectionEnabled(), Boolean.parseBoolean(enableOverdraftProtectionEl.getAttribute("aria-checked")));
        assertEquals(parameters.getIsOverdraftProtectionEnabled(), getCheckedStateByXPath(locators.getEnableOverdraftProtectionHiddenInputXPathString()));
    }

    private void submitAccountCreationForm(AccountCreationParameters parameters) {
        fillAccountCreationForm(parameters);
        waitAndReturnElement(locators.getSaveAccountBy()).click();
        assertTrue(ToastTest.getToastText(this).contains(ACCOUNT_CREATION_SUCCESS_MESSAGE), "Expected the toast message text to contain the account creation success message.");
    }

    @Test
    public void createAccountTest() {
        AuthTest.loginAdmin(this);
        waitAndReturnElement(locators.getDashboardNavigationBy()).click();
        submitAccountCreationForm(AccountCreationParameters.builder()
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
        submitAccountCreationForm(AccountCreationParameters.builder()
            .accountName("via keyboard")
            .accountType(AccountType.SAVINGS_ACCOUNT)
            .balanceCents(1234L)
            .isActive(true)
            .isOverdraftProtectionEnabled(false)
            .build());
    }
}
