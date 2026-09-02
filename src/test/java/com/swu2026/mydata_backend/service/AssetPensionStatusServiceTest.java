package com.swu2026.mydata_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.swu2026.mydata_backend.domain.PensionAccountType;
import com.swu2026.mydata_backend.dto.AssetPensionStatusResponse;
import com.swu2026.mydata_backend.dto.MydataSnapshotRequest;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class AssetPensionStatusServiceTest {

    private final AssetPensionStatusService service = new AssetPensionStatusService();

    @Test
    void 시드_데이터_기준으로_총자산과_예상월연금을_계산한다() {
        MydataSnapshotRequest mydata = new MydataSnapshotRequest();
        mydata.setAnnualGrossSalary(40_800_000L);

        MydataSnapshotRequest.NationalPension nationalPension = new MydataSnapshotRequest.NationalPension();
        nationalPension.setEstimatedMonthlyAmount(320_000L);
        nationalPension.setPaymentStartAge(65);
        nationalPension.setContributionYears(4);
        mydata.setNationalPension(nationalPension);

        MydataSnapshotRequest.RetirementPension retirementPension = new MydataSnapshotRequest.RetirementPension();
        retirementPension.setBalanceAmt(3_200_000L);
        retirementPension.setEvalAmt(3_200_000L);
        retirementPension.setIssueDate(LocalDate.parse("2021-03-15"));
        mydata.setRetirementPension(retirementPension);

        MydataSnapshotRequest.PersonalPensionAccount pensionSavings = new MydataSnapshotRequest.PersonalPensionAccount();
        pensionSavings.setAccountType(PensionAccountType.PENSION_SAVINGS);
        pensionSavings.setAccumAmt(2_000_000L);
        pensionSavings.setEvalAmt(2_050_000L);
        pensionSavings.setEmployerAmt(0L);
        pensionSavings.setEmployeeAmt(2_000_000L);
        pensionSavings.setIssueDate(LocalDate.parse("2022-06-01"));
        pensionSavings.setAnnualContribution(500_000L);

        MydataSnapshotRequest.PersonalPensionAccount irp = new MydataSnapshotRequest.PersonalPensionAccount();
        irp.setAccountType(PensionAccountType.IRP);
        irp.setAccumAmt(2_300_000L);
        irp.setEvalAmt(2_400_000L);
        irp.setEmployerAmt(0L);
        irp.setEmployeeAmt(2_300_000L);
        irp.setIssueDate(LocalDate.parse("2022-06-01"));
        irp.setRcvStartDate(LocalDate.parse("2054-01-01"));
        irp.setAnnualContribution(500_000L);

        mydata.setPersonalPensionAccounts(List.of(pensionSavings, irp));

        MydataSnapshotRequest.SavingsInvestment savingsInvestment = new MydataSnapshotRequest.SavingsInvestment();
        savingsInvestment.setAccounts(List.of(
            account("1", "예금", 20_000_000L),
            account("2", "주식", 7_000_000L),
            account("3", "ETF", 5_000_000L)
        ));
        mydata.setSavingsInvestment(savingsInvestment);

        MydataSnapshotRequest.BankTransaction bankTransaction = new MydataSnapshotRequest.BankTransaction();
        bankTransaction.setSalaryAmt(3_400_000L);
        bankTransaction.setExpenseAmt(2_100_000L);
        mydata.setBankTransaction(bankTransaction);

        AssetPensionStatusResponse response = service.calculate(mydata);

        // 20,000,000 + 7,000,000 + 5,000,000 + 4,450,000(개인연금 평가액) + 3,200,000(퇴직연금 평가액)
        assertThat(response.getTotalAssets()).isEqualTo(39_650_000);
        assertThat(response.getNationalPensionMonthly()).isEqualTo(320_000);
        // 3,200,000 / 240개월
        assertThat(response.getRetirementPensionMonthlyEstimate()).isEqualTo(13_333);
        // 4,450,000 / 240개월
        assertThat(response.getPersonalPensionMonthlyEstimate()).isEqualTo(18_541);
        assertThat(response.getExpectedMonthlyPension()).isEqualTo(351_874);
        assertThat(response.getPersonalPensionAccumAmt()).isEqualTo(4_300_000);
        assertThat(response.getPersonalPensionEmployeeAmt()).isEqualTo(4_300_000);
    }

    private MydataSnapshotRequest.SavingsInvestment.Account account(String accountNum, String prodName, long balanceAmt) {
        MydataSnapshotRequest.SavingsInvestment.Account account = new MydataSnapshotRequest.SavingsInvestment.Account();
        account.setAccountNum(accountNum);
        account.setProdName(prodName);
        account.setBalanceAmt(balanceAmt);
        return account;
    }
}
