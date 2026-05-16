package hu.elte.web.gaborkrisko.seleniumtest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToastTest {
    public static String getToastText(SeleniumTestBase test) {
        final var toastItems = test.waitAndReturnElements(test.locators.getToastItemBy());
        assertEquals(1, toastItems.size(), "Expected exactly one toast message.");
        final var titleEl = test.waitAndReturnElement(test.locators.getToastItemTitleBy());
        return titleEl.getText();
    }
}
