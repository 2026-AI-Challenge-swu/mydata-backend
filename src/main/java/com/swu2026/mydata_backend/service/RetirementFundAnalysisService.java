package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.dto.RetirementFundAnalysisResponse;
import org.springframework.stereotype.Service;

@Service
public class RetirementFundAnalysisService {

    private static final int RETIREMENT_START_AGE = 65;
    // 노후 생활기간 가정(65~85세). AssetPensionStatusService의 연금 월액 환산 가정과 동일하다.
    private static final int RETIREMENT_PAYOUT_YEARS = 20;
    private static final double INFLATION_RATE = 0.025;

    public RetirementFundAnalysisResponse analyze(long targetLivingCost, long expectedMonthlyPension, int currentAge) {
        long monthlyShortfall = Math.max(0, targetLivingCost - expectedMonthlyPension);
        int yearsUntilRetirement = Math.max(0, RETIREMENT_START_AGE - currentAge);

        // 은퇴 시점(65세)부터 20년(85세까지) 매년 물가상승률을 반영해 그 해의 월 부족분을 구하고,
        // 연간 금액(×12)으로 환산해 20년치를 모두 더한다. 66세 이후에도 물가가 계속 오른다고 보는 것.
        double requiredAmount = 0;
        for (int payoutYear = 0; payoutYear < RETIREMENT_PAYOUT_YEARS; payoutYear++) {
            double monthlyShortfallAtYear = monthlyShortfall * Math.pow(1 + INFLATION_RATE, yearsUntilRetirement + payoutYear);
            requiredAmount += monthlyShortfallAtYear * 12;
        }

        return RetirementFundAnalysisResponse.builder()
            .targetLivingCost(targetLivingCost)
            .expectedMonthlyPension(expectedMonthlyPension)
            .monthlyShortfall(monthlyShortfall)
            .requiredAmountAtRetirement(Math.round(requiredAmount))
            .inflationRate(INFLATION_RATE)
            .retirementPayoutYears(RETIREMENT_PAYOUT_YEARS)
            .build();
    }
}
