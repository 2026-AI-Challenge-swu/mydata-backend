package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.dto.AssetPensionStatusResponse;
import com.swu2026.mydata_backend.dto.MydataSnapshotRequest;
import java.util.function.ToLongFunction;
import org.springframework.stereotype.Service;

@Service
public class AssetPensionStatusService {

    private static final int PAYOUT_MONTHS = 20 * 12;

    public AssetPensionStatusResponse calculate(MydataSnapshotRequest mydata) {
        long savingsTotal = mydata.getSavingsInvestment().getAccounts().stream()
            .mapToLong(MydataSnapshotRequest.SavingsInvestment.Account::getBalanceAmt)
            .sum();
        long personalPensionEvalTotal = sumPersonalPensionAccounts(
            mydata, MydataSnapshotRequest.PersonalPensionAccount::getEvalAmt
        );
        long totalAssets = savingsTotal
            + personalPensionEvalTotal
            + mydata.getRetirementPension().getEvalAmt();

        long retirementPensionMonthlyEstimate = mydata.getRetirementPension().getEvalAmt() / PAYOUT_MONTHS;
        long personalPensionMonthlyEstimate = personalPensionEvalTotal / PAYOUT_MONTHS;
        long expectedMonthlyPension = mydata.getNationalPension().getEstimatedMonthlyAmount()
            + retirementPensionMonthlyEstimate
            + personalPensionMonthlyEstimate;

        return AssetPensionStatusResponse.builder()
            .totalAssets(totalAssets)
            .expectedMonthlyPension(expectedMonthlyPension)
            .nationalPensionMonthly(mydata.getNationalPension().getEstimatedMonthlyAmount())
            .retirementPensionMonthlyEstimate(retirementPensionMonthlyEstimate)
            .personalPensionMonthlyEstimate(personalPensionMonthlyEstimate)
            .personalPensionAccumAmt(sumPersonalPensionAccounts(mydata, MydataSnapshotRequest.PersonalPensionAccount::getAccumAmt))
            .personalPensionEmployeeAmt(sumPersonalPensionAccounts(mydata, MydataSnapshotRequest.PersonalPensionAccount::getEmployeeAmt))
            .build();
    }

    private long sumPersonalPensionAccounts(
        MydataSnapshotRequest mydata,
        ToLongFunction<MydataSnapshotRequest.PersonalPensionAccount> mapper
    ) {
        return mydata.getPersonalPensionAccounts().stream()
            .mapToLong(mapper)
            .sum();
    }
}
