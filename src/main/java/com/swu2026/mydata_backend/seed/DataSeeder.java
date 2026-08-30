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
        // 매번 새로 insert하면 재시작할 때마다 문서가 중복 생성되므로,
        // 기존 문서가 있으면 그 id를 그대로 물려받아 save()가 insert 대신 update로 동작하게 함(upsert).
        // 이렇게 해야 코드의 mock 값을 바꾼 뒤 재시작만 해도 DB가 항상 최신 상태로 맞춰짐.
        String existingId = repository.findByPersonaId(KIM_MINJUN_PERSONA_ID)
            .map(PersonaConnectionDocument::getId)
            .orElse(null);

        PersonaConnectionDocument kimMinjun = PersonaConnectionDocument.builder()
            .id(existingId)
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
                            .prodName("주식")
                            .balanceAmt(7_000_000)
                            .build(),
                        PersonaConnectionDocument.SavingsInvestment.Account.builder()
                            .accountNum("110-555-112233")
                            .prodName("ETF")
                            .balanceAmt(5_000_000)
                            .build()
                    ))
                    .build()
            )
            .bankTransaction(
                PersonaConnectionDocument.BankTransaction.builder()
                    .salaryAmt(3_400_000)
                    .expenseAmt(2_100_000)
                    .investmentAmt(400_000L)
                    .build()
            )
            .build();

        repository.save(kimMinjun);
    }
}
