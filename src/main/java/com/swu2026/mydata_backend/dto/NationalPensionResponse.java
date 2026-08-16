package com.swu2026.mydata_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NationalPensionResponse {

    private long estimatedMonthlyAmount;
    private int paymentStartAge;
    private int contributionYears;
}
