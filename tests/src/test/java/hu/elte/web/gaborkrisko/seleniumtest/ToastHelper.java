package hu.elte.web.gaborkrisko.seleniumtest;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToastHelper {
    public static String getToastText(SeleniumTestBase test) {
        final var toastItems = test.waitAndReturnElements(test.locators.getToastItemBy());
        final var hasToast = toastItems.size() >= 1;
        assertTrue(hasToast, "Expected at least one toast message.");
        final var titleEl = test.waitAndReturnElement(test.locators.getToastItemTitleBy());
        return titleEl.getText();
    }

    public static void assertToastContains(SeleniumTestBase test, String expected) {
        final var actual = getToastText(test);
        assertTrue(actual.contains(expected), "Expected the toast message text '%s' to contain '%s'.".formatted(actual, expected));
    }
}
