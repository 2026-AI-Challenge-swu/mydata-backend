package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.dto.TaxSavingAnalysisRequest;
import com.swu2026.mydata_backend.dto.TaxSavingAnalysisResponse;
import java.time.LocalDate;
import java.time.Period;
import org.springframework.stereotype.Service;

@Service
public class TaxSavingAnalysisService {

    // 연금저축 세액공제 한도(연 600만원), 연금저축+개인연금(IRP) 합산 세액공제 한도(연 900만원)
    private static final long PENSION_SAVINGS_DEDUCTION_LIMIT = 6_000_000L;
    private static final long TOTAL_DEDUCTION_LIMIT = 9_000_000L;

    // 총급여 5,500만원 이하 16.5%, 초과 13.2%
    private static final long HIGH_RATE_SALARY_THRESHOLD = 55_000_000L;
    private static final double HIGH_DEDUCTION_RATE = 0.165;
    private static final double LOW_DEDUCTION_RATE = 0.132;

    public TaxSavingAnalysisResponse analyze(TaxSavingAnalysisRequest request) {
        long pensionSavingsAnnual = resolveAnnualContribution(
            request.getPensionSavingsAnnualContribution(),
            request.getPensionSavingsAccumAmt(),
            request.getPensionSavingsIssueDate()
        );
        long personalPensionAnnual = resolveAnnualContribution(
            request.getPersonalPensionAnnualContribution(),
            request.getPersonalPensionEmployeeAmt(),
            request.getPersonalPensionIssueDate()
        );

        double rate = request.getTotalSalary() <= HIGH_RATE_SALARY_THRESHOLD
            ? HIGH_DEDUCTION_RATE
            : LOW_DEDUCTION_RATE;

        long currentEligibleAmount = eligibleAmount(pensionSavingsAnnual, personalPensionAnnual);
        long currentDeductionAmount = Math.round(currentEligibleAmount * rate);

        long recommendedEligibleAmount = TOTAL_DEDUCTION_LIMIT;
        long recommendedDeductionAmount = Math.round(recommendedEligibleAmount * rate);

        return TaxSavingAnalysisResponse.builder()
            .totalSalary(request.getTotalSalary())
            .deductionRate(rate)
            .currentPensionSavingsAnnualContribution(pensionSavingsAnnual)
            .currentPersonalPensionAnnualContribution(personalPensionAnnual)
            .currentEligibleAmount(currentEligibleAmount)
            .currentDeductionAmount(currentDeductionAmount)
            .recommendedEligibleAmount(recommendedEligibleAmount)
            .recommendedDeductionAmount(recommendedDeductionAmount)
            .increaseAmount(recommendedDeductionAmount - currentDeductionAmount)
            .build();
    }

    private long eligibleAmount(long pensionSavingsAnnual, long personalPensionAnnual) {
        long eligiblePensionSavings = Math.min(pensionSavingsAnnual, PENSION_SAVINGS_DEDUCTION_LIMIT);
        return Math.min(eligiblePensionSavings + personalPensionAnnual, TOTAL_DEDUCTION_LIMIT);
    }

    private long resolveAnnualContribution(Long override, long accumulatedAmount, LocalDate issueDate) {
        if (override != null) {
            return override;
        }
        return accumulatedAmount / elapsedYearsSince(issueDate);
    }

    private int elapsedYearsSince(LocalDate issueDate) {
        int years = Period.between(issueDate, LocalDate.now()).getYears();
        return Math.max(years, 1);
    }
}
