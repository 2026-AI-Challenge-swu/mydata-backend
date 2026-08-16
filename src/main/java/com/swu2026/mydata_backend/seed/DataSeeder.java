package com.swu2026.mydata_backend.seed;

import com.swu2026.mydata_backend.domain.PersonaConnectionDocument;
import com.swu2026.mydata_backend.repository.PersonaConnectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final String KIM_MINJUN_PERSONA_ID = "kimMinjun";

    private final PersonaConnectionRepository repository;

    @Override
    public void run(String... args) {
        if (repository.findByPersonaId(KIM_MINJUN_PERSONA_ID).isPresent()) {
            return;
        }

        PersonaConnectionDocument kimMinjun = PersonaConnectionDocument.builder()
            .personaId(KIM_MINJUN_PERSONA_ID)
            .nationalPension(
                PersonaConnectionDocument.NationalPension.builder()
                    .estimatedMonthlyAmount(320_000)
                    .paymentStartAge(65)
                    .contributionYears(4)
                    .build()
            )
            .retirementPension(
                PersonaConnectionDocument.RetirementPension.builder()
                    .balanceAmt(3_200_000)
                    .evalAmt(3_200_000)
                    .issueDate("2021-03-15")
                    .build()
            )
            .personalPension(
                PersonaConnectionDocument.PersonalPension.builder()
                    .accumAmt(4_300_000)
                    .evalAmt(4_450_000)
                    .employerAmt(0)
                    .employeeAmt(4_300_000)
                    .issueDate("2022-06-01")
                    .rcvStartDate("2054-01-01")
                    .build()
            )
            .savingsInvestment(
                PersonaConnectionDocument.SavingsInvestment.builder()
                    .accounts(List.of(
                        PersonaConnectionDocument.SavingsInvestment.Account.builder()
                            .accountNum("110-123-456789")
                            .prodName("예금")
                            .balanceAmt(20_000_000)
                            .build(),
                        PersonaConnectionDocument.SavingsInvestment.Account.builder()
                            .accountNum("110-987-654321")
                            .prodName("주식/ETF")
                            .balanceAmt(12_000_000)
                            .build()
                    ))
                    .build()
            )
            .build();

        repository.save(kimMinjun);
    }
}
