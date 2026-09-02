package com.swu2026.mydata_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AssetPensionStatusResponse {

    private long totalAssets;
    private long expectedMonthlyPension;

    private long nationalPensionMonthly;
    private long retirementPensionMonthlyEstimate;
    private long personalPensionMonthlyEstimate;

    private long personalPensionAccumAmt;
    private long personalPensionEmployeeAmt;
}
