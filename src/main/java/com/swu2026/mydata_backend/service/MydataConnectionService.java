package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.domain.PersonaConnectionDocument;
import com.swu2026.mydata_backend.dto.BankTransactionResponse;
import com.swu2026.mydata_backend.dto.EmploymentResponse;
import com.swu2026.mydata_backend.dto.IdentityResponse;
import com.swu2026.mydata_backend.dto.IncomeResponse;
import com.swu2026.mydata_backend.dto.NationalPensionResponse;
import com.swu2026.mydata_backend.dto.PersonalPensionResponse;
import com.swu2026.mydata_backend.dto.RetirementPensionResponse;
import com.swu2026.mydata_backend.dto.SavingsInvestmentResponse;
import com.swu2026.mydata_backend.exception.MydataConnectionException;
import com.swu2026.mydata_backend.repository.PersonaConnectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MydataConnectionService {

    private static final String DEFAULT_PERSONA_ID = "kimMinjun";
    private static final String SCENARIO_FAILURE = "failure";
    private static final String SCENARIO_PARTIAL_FAILURE = "partialFailure";

    private final PersonaConnectionRepository repository;

    public IdentityResponse getIdentity(String scenario) {
        if (SCENARIO_FAILURE.equals(scenario)) {
            throw authFailure();
        }
        return toIdentityResponse(loadPersona().getIdentity());
    }

    public IncomeResponse getIncome(String scenario) {
        if (SCENARIO_FAILURE.equals(scenario)) {
            throw authFailure();
        }
        return toIncomeResponse(loadPersona().getIncomeInfo());
    }

    public EmploymentResponse getEmployment(String scenario) {
        if (SCENARIO_FAILURE.equals(scenario)) {
            throw authFailure();
        }
        return toEmploymentResponse(loadPersona().getEmploymentInfo());
    }

    public NationalPensionResponse getNationalPension(String scenario) {
        if (SCENARIO_FAILURE.equals(scenario)) {
            throw authFailure();
        }
        if (SCENARIO_PARTIAL_FAILURE.equals(scenario)) {
            throw nationalPensionFailure();
        }
        return toNationalPensionResponse(loadPersona().getNationalPension());
    }

    public RetirementPensionResponse getRetirementPension(String scenario) {
        if (SCENARIO_FAILURE.equals(scenario)) {
            throw authFailure();
        }
        return toRetirementPensionResponse(loadPersona().getRetirementPension());
    }

    public PersonalPensionResponse getPersonalPension(String scenario) {
        if (SCENARIO_FAILURE.equals(scenario)) {
            throw authFailure();
        }
        return toPersonalPensionResponse(loadPersona().getPersonalPensionAccounts());
    }

    public SavingsInvestmentResponse getSavingsInvestment(String scenario) {
        if (SCENARIO_FAILURE.equals(scenario)) {
            throw authFailure();
        }
        return toSavingsInvestmentResponse(loadPersona().getSavingsInvestment());
    }

    public BankTransactionResponse getBankTransaction(String scenario) {
        if (SCENARIO_FAILURE.equals(scenario)) {
            throw authFailure();
        }
        return toBankTransactionResponse(loadPersona().getBankTransaction());
    }

    public IdentityResponse retryIdentity() {
        return getIdentity("success");
    }

    public IncomeResponse retryIncome() {
        return getIncome("success");
    }

    public EmploymentResponse retryEmployment() {
        return getEmployment("success");
    }

    public NationalPensionResponse retryNationalPension() {
        throw nationalPensionFailure();
    }

    public RetirementPensionResponse retryRetirementPension() {
        return getRetirementPension("success");
    }

    public PersonalPensionResponse retryPersonalPension() {
        return getPersonalPension("success");
    }

    public SavingsInvestmentResponse retrySavingsInvestment() {
        return getSavingsInvestment("success");
    }

    public BankTransactionResponse retryBankTransaction() {
        return getBankTransaction("success");
    }

    private PersonaConnectionDocument loadPersona() {
        return repository.findByPersonaId(DEFAULT_PERSONA_ID)
            .orElseThrow(() -> new IllegalStateException("시드 데이터가 없습니다: " + DEFAULT_PERSONA_ID));
    }

    private MydataConnectionException authFailure() {
        return new MydataConnectionException("인증 실패", true);
    }

    private MydataConnectionException nationalPensionFailure() {
        return new MydataConnectionException("국민연금공단 연계 실패: 이용기관 등록 심사 미완료", false);
    }

    private IdentityResponse toIdentityResponse(PersonaConnectionDocument.Identity source) {
        return IdentityResponse.builder()
            .name(source.getName())
            .birthYear(source.getBirthYear())
            .gender(source.getGender())
            .build();
    }

    private IncomeResponse toIncomeResponse(PersonaConnectionDocument.IncomeInfo source) {
        return IncomeResponse.builder()
            .annualGrossSalary(source.getAnnualGrossSalary())
            .build();
    }

    private EmploymentResponse toEmploymentResponse(PersonaConnectionDocument.EmploymentInfo source) {
        return EmploymentResponse.builder()
            .jobLabel(source.getJobLabel())
            .build();
    }

    private NationalPensionResponse toNationalPensionResponse(PersonaConnectionDocument.NationalPension source) {
        return NationalPensionResponse.builder()
            .estimatedMonthlyAmount(source.getEstimatedMonthlyAmount())
            .paymentStartAge(source.getPaymentStartAge())
            .contributionYears(source.getContributionYears())
            .build();
    }

    private RetirementPensionResponse toRetirementPensionResponse(PersonaConnectionDocument.RetirementPension source) {
        return RetirementPensionResponse.builder()
            .balanceAmt(source.getBalanceAmt())
            .evalAmt(source.getEvalAmt())
            .issueDate(source.getIssueDate())
            .build();
    }

    private PersonalPensionResponse toPersonalPensionResponse(
        List<PersonaConnectionDocument.PersonalPensionAccount> source
    ) {
        List<PersonalPensionResponse.Account> accounts = source.stream()
            .map(account -> PersonalPensionResponse.Account.builder()
                .accountType(account.getAccountType().name())
                .accumAmt(account.getAccumAmt())
                .evalAmt(account.getEvalAmt())
                .employerAmt(account.getEmployerAmt())
                .employeeAmt(account.getEmployeeAmt())
                .issueDate(account.getIssueDate())
                .rcvStartDate(account.getRcvStartDate())
                .build())
            .toList();

        return PersonalPensionResponse.builder()
            .accounts(accounts)
            .build();
    }

    private SavingsInvestmentResponse toSavingsInvestmentResponse(
        PersonaConnectionDocument.SavingsInvestment source
    ) {
        List<SavingsInvestmentResponse.Account> accounts = source.getAccounts().stream()
            .map(account -> SavingsInvestmentResponse.Account.builder()
                .accountNum(account.getAccountNum())
                .prodName(account.getProdName())
                .balanceAmt(account.getBalanceAmt())
                .build())
            .toList();

        return SavingsInvestmentResponse.builder()
            .accounts(accounts)
            .build();
    }

    private BankTransactionResponse toBankTransactionResponse(PersonaConnectionDocument.BankTransaction source) {
        return BankTransactionResponse.builder()
            .salaryAmt(source.getSalaryAmt())
            .expenseAmt(source.getExpenseAmt())
            .investmentAmt(source.getInvestmentAmt())
            .build();
    }
}
