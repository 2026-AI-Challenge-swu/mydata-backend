package com.swu2026.mydata_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TaxSavingAnalysisResponse {

    private long totalSalary;
    private double deductionRate;

    private long currentPersonalPensionAnnualContribution;
    private long currentRetirementPensionAnnualContribution;
    private long currentEligibleAmount;
    private long currentDeductionAmount;

    private long recommendedEligibleAmount;
    private long recommendedDeductionAmount;

    private long increaseAmount;
}
