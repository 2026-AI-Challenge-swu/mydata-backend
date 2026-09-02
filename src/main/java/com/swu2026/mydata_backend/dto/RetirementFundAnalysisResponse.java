package com.swu2026.mydata_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetirementFundAnalysisResponse {

    private long targetLivingCost;
    private long expectedMonthlyPension;
    private long monthlyShortfall;
    private long requiredAmountAtRetirement;
    private double inflationRate;
    private int retirementPayoutYears;
}
