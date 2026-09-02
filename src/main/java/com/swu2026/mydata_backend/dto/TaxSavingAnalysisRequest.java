package com.swu2026.mydata_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaxSavingAnalysisRequest {

    // 세전 연간 총급여 (세액공제 구간 5,500만원 기준 판단에 사용)
    @NotNull
    private Long totalSalary;

    // 연금저축 세액공제 대상 연간 납입액
    @NotNull
    private Long pensionSavingsAnnualContribution;

    // 개인연금(IRP) 세액공제 대상 연간 납입액
    @NotNull
    private Long personalPensionAnnualContribution;
}
