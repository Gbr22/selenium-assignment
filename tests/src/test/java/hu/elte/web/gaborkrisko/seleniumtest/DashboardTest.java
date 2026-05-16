package hu.elte.web.gaborkrisko.seleniumtest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardTest extends SeleniumTestBase {
    private List<WebElement> getPinnedAccounts() {
        return getElementsByXPath(locators.getPinnedAccountNameXPathString());
    }
    private List<String> getPinnedAccountIds(List<WebElement> elements) {
        return elements.stream().map(e->e.getAttribute("data-account-id")).toList();
    }
    private List<String> getPinnedAccountIds() {
        return getPinnedAccountIds(getPinnedAccounts());
    }

    @Test
    public void pinnedAccountsDragAndDropTest() {
        AuthTest.loginAdmin(this);
        List.of("one", "two", "three").forEach(name->{
            BankAccountTest.createAccount(this, AccountCreationParameters.builder()
                .accountName(name)
                .accountType(AccountType.CHECKING_ACCOUNT)
                .balanceCents(1234L)
                .isActive(true)
                .isOverdraftProtectionEnabled(false)
                .build());
        });
        waitAndReturnElement(locators.getDashboardNavigationBy()).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locators.getPinnedAccountsXPathString())));
        scrollElementIntoView(locators.getPinnedAccountsXPathString());
        final var oldAccounts = getPinnedAccounts();
        final var oldAccountIds = getPinnedAccountIds(oldAccounts);
        Actions actions = new Actions(driver);
        actions.dragAndDrop(oldAccounts.get(0), oldAccounts.get(2)).perform();
        final var newAccountIds = getPinnedAccountIds();
        assertAll(
            ()->assertEquals(oldAccountIds.get(0), newAccountIds.get(2)),
            ()->assertEquals(oldAccountIds.get(1), newAccountIds.get(0)),
            ()->assertEquals(oldAccountIds.get(2), newAccountIds.get(1))
        );
    }
}
