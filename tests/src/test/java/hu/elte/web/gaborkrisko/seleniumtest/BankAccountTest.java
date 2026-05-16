package hu.elte.web.gaborkrisko.seleniumtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.support.ParameterDeclarations;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import com.github.javafaker.Faker;

public class BankAccountTest extends SeleniumTestBase {
    private final static String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully!";
    private final static String ACCOUNT_DELETION_SUCCESS_MESSAGE = "Account deleted successfully.";

    private static String formatBalance(long balanceCents) {
        long whole = balanceCents / 100;
        long cents = balanceCents - (whole * 100);
        String centsString = cents < 10 ? "0%d".formatted(cents) : "%d".formatted(cents);
        return "%d.%s".formatted(whole, centsString);
    }

    private static void submitAccountCreationForm(SeleniumTestBase test, AccountCreationParameters parameters) {
        final var locators = test.locators;
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

        final var enableOverdraftProtectionEl = test.waitAndReturnElement(locators.getEnableOverdraftProtectionCheckboxBy());
        if (parameters.getIsOverdraftProtectionEnabled()) {
            enableOverdraftProtectionEl.click();
        }
        assertEquals(parameters.getIsOverdraftProtectionEnabled(), Boolean.parseBoolean(enableOverdraftProtectionEl.getAttribute("aria-checked")));
        assertEquals(parameters.getIsOverdraftProtectionEnabled(), test.getCheckedStateByXPath(locators.getEnableOverdraftProtectionHiddenInputXPathString()));
        
        test.waitAndReturnElement(test.locators.getSaveAccountBy()).click();
        ToastHelper.assertToastContains(test, ACCOUNT_CREATION_SUCCESS_MESSAGE);
    }

    public static void createAccount(SeleniumTestBase test, AccountCreationParameters parameters) {
        test.waitAndReturnElement(test.locators.getDashboardNavigationBy()).click();
        final var addAccountEl = test.waitAndReturnElement(test.locators.getAddAccountBy());
        addAccountEl.sendKeys(Keys.ENTER);
        BankAccountTest.submitAccountCreationForm(test, parameters);
    }

    @Test
    public void createAccountTest() {
        AuthTest.loginAdmin(this);
        BankAccountTest.createAccount(this, AccountCreationParameters.builder()
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
        waitAndReturnElement(locators.getAccountsNavigationBy()).click();
        this.wait.until(ExpectedConditions.visibilityOfElementLocated(locators.getAccountsTableBy()));
        dispatchKeyboardEvent("keydown", "N");
        BankAccountTest.submitAccountCreationForm(this, AccountCreationParameters.builder()
            .accountName("via keyboard")
            .accountType(AccountType.SAVINGS_ACCOUNT)
            .balanceCents(1234L)
            .isActive(true)
            .isOverdraftProtectionEnabled(false)
            .build());
    }

    private static class AccountParametersProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ParameterDeclarations parameters, ExtensionContext context) {
            final var items = new ArrayList<AccountCreationParameters>();
            final var random = ThreadLocalRandom.current();
            final var accountTypes = AccountType.values();
            final var faker = new Faker();
            for (int i=0; i < 5; i++) {
                items.add(AccountCreationParameters.builder()
                    .accountName(faker.funnyName().name())
                    .balanceCents(random.nextLong(1, 1000000))
                    .isActive(random.nextBoolean())
                    .isOverdraftProtectionEnabled(random.nextBoolean())
                    .accountType(accountTypes[random.nextInt(accountTypes.length)])
                    .build());
            }
            return items.stream().map(e->Arguments.of(e));
        }
    }

    @ParameterizedTest
    @ArgumentsSource(AccountParametersProvider.class)
    public void createAndDeleteAccountTest(AccountCreationParameters parameters) {
        AuthTest.loginAdmin(this);
        BankAccountTest.createAccount(this, parameters);
        waitAndReturnElement(locators.getAccountsNavigationBy()).click();
        final var searchEl = waitAndReturnElement(locators.getAccountSearchInputBy());
        searchEl.sendKeys(parameters.getAccountName());
        assertTrue(waitAndReturnElement(locators.getAccountNameCellBy()).getText().contains(parameters.getAccountName()));
        waitAndReturnElement(locators.getDeleteAccountBy()).click();
        waitAndReturnElement(locators.getConfirmDeleteActionBy()).click();
        ToastHelper.assertToastContains(this, ACCOUNT_DELETION_SUCCESS_MESSAGE);
    }
}
