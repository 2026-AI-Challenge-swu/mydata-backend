package com.swu2026.mydata_backend.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BankTransactionResponse {

    private long salaryAmt;
    private long expenseAmt;
    private long investmentAmt;
}
