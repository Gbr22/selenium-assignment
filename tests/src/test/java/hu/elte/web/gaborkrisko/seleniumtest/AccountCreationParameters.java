package hu.elte.web.gaborkrisko.seleniumtest;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccountCreationParameters {
    private String accountName;
    private AccountType accountType;
    private Long balanceCents;
    private Boolean isActive;
    private Boolean isOverdraftProtectionEnabled;
}
