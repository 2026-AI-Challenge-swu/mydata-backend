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
}
