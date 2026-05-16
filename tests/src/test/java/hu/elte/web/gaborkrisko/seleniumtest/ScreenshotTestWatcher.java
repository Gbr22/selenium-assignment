package hu.elte.web.gaborkrisko.seleniumtest;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class ScreenshotTestWatcher implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        SeleniumTestBase test = (SeleniumTestBase) context.getRequiredTestInstance();
        test.saveScreenshot();
    }    
}
