package com.swu2026.mydata_backend.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class TaxSavingAnalysisRequest {

    @NotNull
    private Long totalSalary;

    @NotNull
    private Long personalPensionAccumAmt;

    @NotNull
    private LocalDate personalPensionIssueDate;

    @NotNull
    private Long retirementPensionBalanceAmt;

    @NotNull
    private LocalDate retirementPensionIssueDate;
    private Long personalPensionAnnualContribution;
    private Long retirementPensionAnnualContribution;
}
