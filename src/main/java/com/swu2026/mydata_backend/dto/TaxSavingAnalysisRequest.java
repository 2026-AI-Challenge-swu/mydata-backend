package com.swu2026.mydata_backend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class TaxSavingAnalysisRequest {

    @NotNull
    private Long totalSalary;

    // 연금저축
    @NotNull
    private Long pensionSavingsAccumAmt;

    @NotNull
    private LocalDate pensionSavingsIssueDate;

    private Long pensionSavingsAnnualContribution;

    // 개인연금(IRP)
    @NotNull
    private Long personalPensionEmployeeAmt;

    @NotNull
    private LocalDate personalPensionIssueDate;

    private Long personalPensionAnnualContribution;
}
