package hu.elte.web.gaborkrisko.seleniumtest;

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

    private void createAccount(String accountName, AccountType accountType, long balanceCents) {
        AuthTest.loginAdmin(this);
        final var addAccountEl = waitAndReturnElement(locators.getAddAccountBy());
        addAccountEl.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(locators.getAccountFormBy()));
        
        final var accountNameEl = waitAndReturnElement(locators.getAccountNameInputBy());
        accountNameEl.sendKeys(accountName);
        
        final var accountTypeBy = waitAndReturnElement(locators.getAccountTypeBy());
        accountTypeBy.click();
        final var option = waitAndReturnElement(locators.getAccountTypeSelectOptions().get(accountType));
        option.click();

        final var initialBalanceEl = waitAndReturnElement(locators.getInitialBalanceInput());
        initialBalanceEl.sendKeys(formatBalance(balanceCents));
        
        final var saveAccount = waitAndReturnElement(locators.getSaveAccountBy());
        saveAccount.click();

        assertTrue(ToastTest.getToastText(this).contains(ACCOUNT_CREATION_SUCCESS_MESSAGE), "Expected the toast message text to contain the account creation success message.");
    }

    @Test
    public void createAccountTest() {
        createAccount("test", AccountType.CREDIT_CARD, 123);
    }
}
