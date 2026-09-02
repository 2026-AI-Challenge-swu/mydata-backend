package com.swu2026.mydata_backend.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PersonalPensionResponse {

    private List<Account> accounts;

    @Getter
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Account {
        private String accountType;
        private long accumAmt;
        private long evalAmt;
        private long employerAmt;
        private long employeeAmt;
        private String issueDate;
        private String rcvStartDate;
    }
}
