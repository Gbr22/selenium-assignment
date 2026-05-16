package hu.elte.web.gaborkrisko.seleniumtest;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TestConfig {
    private final String baseURL;
    private final String driverURL;
    private final String loginURL;
    private final String adminUsername;
    private final String adminPassword;
}
