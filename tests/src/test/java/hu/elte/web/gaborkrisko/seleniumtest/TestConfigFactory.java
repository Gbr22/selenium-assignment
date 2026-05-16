package hu.elte.web.gaborkrisko.seleniumtest;

public class TestConfigFactory {
    public TestConfig createFromEnvironment() {
        return TestConfig.builder()
            .baseURL(System.getenv("TEST_BASE_URL"))
            .loginURL(System.getenv("TEST_BASE_URL"))
            .driverURL(System.getenv("TEST_DRIVER_URL"))
            .adminUsername(System.getenv("TEST_ADMIN_USERNAME"))
            .adminPassword(System.getenv("TEST_ADMIN_PASSWORD"))
            .build();
    }
}
