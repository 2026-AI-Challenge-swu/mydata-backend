package com.swu2026.mydata_backend.dto;

import com.swu2026.mydata_backend.domain.PensionAccountType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class MydataSnapshotRequest {

    @NotNull
    private Long annualGrossSalary;

    @NotNull
    @Valid
    private NationalPension nationalPension;

    @NotNull
    @Valid
    private RetirementPension retirementPension;

    @NotEmpty
    private List<@Valid PersonalPensionAccount> personalPensionAccounts;

    @NotNull
    @Valid
    private SavingsInvestment savingsInvestment;

    @NotNull
    @Valid
    private BankTransaction bankTransaction;

    @Data
    public static class NationalPension {
        @NotNull
        private Long estimatedMonthlyAmount;
        private Integer paymentStartAge;
        private Integer contributionYears;
    }

    @Data
    public static class RetirementPension {
        @NotNull
        private Long balanceAmt;
        @NotNull
        private Long evalAmt;
        @NotNull
        private LocalDate issueDate;
    }

    // IRP와 연금저축은 세액공제 한도(연금저축 600만원 단독 / 둘 합산 900만원)가 달라서
    // 계좌 하나가 아니라 리스트로 관리 — 어느 상품유형인지는 accountType으로 구분.
    @Data
    public static class PersonalPensionAccount {
        @NotNull
        private PensionAccountType accountType;
        @NotNull
        private Long accumAmt;
        @NotNull
        private Long evalAmt;
        private Long employerAmt;
        @NotNull
        private Long employeeAmt;
        @NotNull
        private LocalDate issueDate;
        private LocalDate rcvStartDate;

        // 세액공제 대상 연간 납입액
        @NotNull
        private Long annualContribution;
    }

    @Data
    public static class SavingsInvestment {
        @NotEmpty
        private List<@Valid Account> accounts;

        @Data
        public static class Account {
            private String accountNum;
            private String prodName;
            @NotNull
            private Long balanceAmt;
        }
    }

    @Data
    public static class BankTransaction {
        @NotNull
        private Long salaryAmt;
        private Long expenseAmt;
    }
}
