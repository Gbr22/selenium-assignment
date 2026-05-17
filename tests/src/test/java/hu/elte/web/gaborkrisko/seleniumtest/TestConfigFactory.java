package hu.elte.web.gaborkrisko.seleniumtest;

import java.util.function.Supplier;

import org.openqa.selenium.Dimension;

public class TestConfigFactory {
    private <T> T tryOrDefault(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        }
        catch (Exception e) {
            return defaultValue;
        }
    }

    public TestConfig createFromEnvironment() {
        return TestConfig.builder()
            .baseURL(System.getenv("TEST_BASE_URL"))
            .loginURL(System.getenv("TEST_BASE_URL"))
            .driverURL(System.getenv("TEST_DRIVER_URL"))
            .adminUsername(System.getenv("TEST_ADMIN_USERNAME"))
            .adminPassword(System.getenv("TEST_ADMIN_PASSWORD"))
            .windowSize(tryOrDefault(
                ()->new Dimension(
                    Integer.parseInt(System.getenv("TEST_WINDOW_WIDTH")),
                    Integer.parseInt(System.getenv("TEST_WINDOW_HEIGHT"))
                ),
                null
            ))
            .isHeadless(tryOrDefault(
                ()->Boolean.parseBoolean(System.getenv("TEST_HEADLESS")),
                true
            ))
            .userAgent(System.getenv("TEST_USER_AGENT"))
            .build();
    }
}
