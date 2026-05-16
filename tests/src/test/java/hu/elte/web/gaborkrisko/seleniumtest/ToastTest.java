package hu.elte.web.gaborkrisko.seleniumtest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToastTest {
    public static String getToastText(SeleniumTestBase test) {
        final var toastItems = test.waitAndReturnElements(test.locators.getToastItemBy());
        final var hasToast = toastItems.size() >= 1;
        assertTrue(hasToast, "Expected at least one toast message.");
        final var titleEl = test.waitAndReturnElement(test.locators.getToastItemTitleBy());
        return titleEl.getText();
    }
}
