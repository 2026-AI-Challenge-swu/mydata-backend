package com.swu2026.mydata_backend.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "persona_connections")
public class PersonaConnectionDocument {

    @Id
    private String id;

    private String personaId;

    private NationalPension nationalPension;
    private RetirementPension retirementPension;
    private PersonalPension personalPension;
    private SavingsInvestment savingsInvestment;
    private BankTransaction bankTransaction;

    @Getter
    @Builder
    public static class NationalPension {
        private long estimatedMonthlyAmount;
        private int paymentStartAge;
        private int contributionYears;
    }

    @Getter
    @Builder
    public static class RetirementPension {
        private long balanceAmt;
        private long evalAmt;
        private String issueDate;
    }

    @Getter
    @Builder
    public static class PersonalPension {
        private long accumAmt;
        private long evalAmt;
        private long employerAmt;
        private long employeeAmt;
        private String issueDate;
        private String rcvStartDate;
    }

    @Getter
    @Builder
    public static class SavingsInvestment {
        private List<Account> accounts;

        @Getter
        @Builder
        public static class Account {
            private String accountNum;
            private String prodName;
            private long balanceAmt;
        }
    }

    // 은행-004(거래내역) 스펙 기반. 실제로는 거래 목록을 패턴 분석해서 급여/소비를 추정해야 하지만,
    // 정의서(S1-07~09)에도 계산 방식이 TBD라 이번엔 월급·소비 집계값만 바로 mock으로 둠
    @Getter
    @Builder
    public static class BankTransaction {
        private long salaryAmt;
        private long expenseAmt;
        // Long(boxed): 기존에 저장된 문서엔 이 필드가 없어서 역직렬화 시 null이 들어올 수 있음.
        // long(원시타입)이면 null을 못 받아서 DataSeeder가 기존 문서를 읽는 순간 예외가 남 — 필드 추가 때마다 겪을 문제라 boxed로 둠.
        private Long investmentAmt;
    }
}
