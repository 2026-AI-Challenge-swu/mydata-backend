package com.swu2026.mydata_backend.dto;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonalPensionResponse {

    private long accumAmt;
    private long evalAmt;
    private long employerAmt;
    private long employeeAmt;
    private String issueDate;
    private String rcvStartDate;
}
