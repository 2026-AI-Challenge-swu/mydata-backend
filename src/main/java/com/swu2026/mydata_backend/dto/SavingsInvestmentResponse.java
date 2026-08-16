package com.swu2026.mydata_backend.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SavingsInvestmentResponse {

    private List<Account> accounts;

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Account {
        private String accountNum;
        private String prodName;
        private long balanceAmt;
    }
}
