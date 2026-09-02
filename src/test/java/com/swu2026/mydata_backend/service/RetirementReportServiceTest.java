package com.swu2026.mydata_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.swu2026.mydata_backend.domain.Gender;
import com.swu2026.mydata_backend.domain.InvestmentProfileType;
import com.swu2026.mydata_backend.domain.PensionAccountType;
import com.swu2026.mydata_backend.dto.AssetPensionStatusResponse;
import com.swu2026.mydata_backend.dto.FutureAssetSimulationResponse;
import com.swu2026.mydata_backend.dto.InvestmentProfileResponse;
import com.swu2026.mydata_backend.dto.MydataSnapshotRequest;
import com.swu2026.mydata_backend.dto.PortfolioRecommendationResponse;
import com.swu2026.mydata_backend.dto.ReportRequest;
import com.swu2026.mydata_backend.dto.ReportResponse;
import com.swu2026.mydata_backend.dto.RetirementFundAnalysisResponse;
import com.swu2026.mydata_backend.dto.RetirementReportRequest;
import com.swu2026.mydata_backend.dto.RetirementReportResponse;
import com.swu2026.mydata_backend.dto.SurveyAnswerRequest;
import com.swu2026.mydata_backend.dto.TaxSavingAnalysisRequest;
import com.swu2026.mydata_backend.dto.TaxSavingAnalysisResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RetirementReportServiceTest {

    @Mock
    private SurveyResponseService surveyResponseService;
    @Mock
    private AssetPensionStatusService assetPensionStatusService;
    @Mock
    private PortfolioRecommendationService portfolioRecommendationService;
    @Mock
    private RetirementFundAnalysisService retirementFundAnalysisService;
    @Mock
    private FutureAssetSimulationService futureAssetSimulationService;
    @Mock
    private TaxSavingAnalysisService taxSavingAnalysisService;
    @Mock
    private ReportService reportService;

    @InjectMocks
    private RetirementReportService service;

    @Test
    void 협력_서비스_결과를_모아_하나의_응답으로_조립한다() {
        InvestmentProfileResponse investmentProfile = InvestmentProfileResponse.builder()
            .totalScore(17)
            .type("STABLE_SEEKING")
            .grade(2)
            .emoji("🌱")
            .officialName("안정추구형")
            .nickname("천천히 쌓는 새싹형")
            .description("설명")
            .build();
        given(surveyResponseService.submit(any())).willReturn(investmentProfile);

        MydataSnapshotRequest mydata = mydata();

        AssetPensionStatusResponse assetPensionStatus = AssetPensionStatusResponse.builder()
            .totalAssets(39_650_000)
            .expectedMonthlyPension(1_500_000)
            .build();
        given(assetPensionStatusService.calculate(mydata)).willReturn(assetPensionStatus);

        PortfolioRecommendationResponse recommendedPortfolio = PortfolioRecommendationResponse.builder()
            .profileType("STABLE_SEEKING")
            .dcDefaultAllocationDescription("원리금보장 60% + 채권형 40%")
            .compositions(List.of(
                PortfolioRecommendationResponse.CompositionItem.builder().category("연금저축 주식").weightPercent(20).build(),
                PortfolioRecommendationResponse.CompositionItem.builder().category("연금저축 채권").weightPercent(25).build()
            ))
            .recommendationReasons(List.of("문구1", "문구2", "문구3"))
            .expectedAnnualReturnRate(0.05)
            .build();
        given(portfolioRecommendationService.recommend(InvestmentProfileType.STABLE_SEEKING, 45, Gender.FEMALE))
            .willReturn(recommendedPortfolio);

        RetirementFundAnalysisResponse retirementFundAnalysis = RetirementFundAnalysisResponse.builder()
            .targetLivingCost(2_500_000)
            .expectedMonthlyPension(1_500_000)
            .monthlyShortfall(1_000_000)
            .requiredAmountAtRetirement(240_000_000L)
            .inflationRate(0.025)
            .retirementPayoutYears(20)
            .build();
        given(retirementFundAnalysisService.analyze(2_500_000L, 1_500_000L, 45)).willReturn(retirementFundAnalysis);

        FutureAssetSimulationResponse futureAssetSimulation = FutureAssetSimulationResponse.builder()
            .currentAge(45)
            .targetAge(65)
            .points(List.of(
                FutureAssetSimulationResponse.Point.builder()
                    .age(45).maintainAmount(39_650_000).plus20Amount(39_650_000).plus40Amount(39_650_000).build(),
                FutureAssetSimulationResponse.Point.builder()
                    .age(65).maintainAmount(80_000_000).plus20Amount(120_000_000).plus40Amount(150_000_000).build()
            ))
            .build();
        given(futureAssetSimulationService.simulate(45, 39_650_000L, 0.05)).willReturn(futureAssetSimulation);

        TaxSavingAnalysisResponse taxSavingAnalysis = TaxSavingAnalysisResponse.builder()
            .totalSalary(40_800_000).increaseAmount(1_000_000).build();
        given(taxSavingAnalysisService.analyze(any())).willReturn(taxSavingAnalysis);

        ReportResponse aiReport = new ReportResponse();
        aiReport.setTotalComment("총평");
        given(reportService.generateReport(any())).willReturn(aiReport);

        RetirementReportRequest request = new RetirementReportRequest();
        SurveyAnswerRequest.Answer answer = new SurveyAnswerRequest.Answer();
        answer.setQuestionId("q1");
        answer.setSelectedOrder(1);
        request.setSurveyAnswers(List.of(answer));
        request.setCurrentAge(45);
        request.setGender(Gender.FEMALE);
        request.setMydata(mydata);

        RetirementReportResponse response = service.generate(request);

        assertThat(response.getInvestmentProfile()).isSameAs(investmentProfile);
        assertThat(response.getAssetPensionStatus()).isSameAs(assetPensionStatus);
        assertThat(response.getRetirementFundAnalysis()).isSameAs(retirementFundAnalysis);
        assertThat(response.getRecommendedPortfolio()).isSameAs(recommendedPortfolio);
        assertThat(response.getFutureAssetSimulation()).isSameAs(futureAssetSimulation);
        assertThat(response.getAiReport()).isSameAs(aiReport);

        // targetLivingCost 미입력 시 기본값(250만원)이 쓰였는지 검증
        verify(retirementFundAnalysisService).analyze(2_500_000L, 1_500_000L, 45);
        verify(futureAssetSimulationService).simulate(45, 39_650_000L, 0.05);

        ArgumentCaptor<TaxSavingAnalysisRequest> taxCaptor = ArgumentCaptor.forClass(TaxSavingAnalysisRequest.class);
        verify(taxSavingAnalysisService).analyze(taxCaptor.capture());
        TaxSavingAnalysisRequest taxRequest = taxCaptor.getValue();
        // mydata.annualGrossSalary(세전 연봉)를 그대로 쓰는지 검증 — bankTransaction.salaryAmt*12(2,940만원)와는 다른 값
        assertThat(taxRequest.getTotalSalary()).isEqualTo(42_000_000L);
        // personalPensionAccounts를 accountType별로 합산해 전달하는지 검증
        assertThat(taxRequest.getPensionSavingsAnnualContribution()).isEqualTo(900_000L);
        assertThat(taxRequest.getPersonalPensionAnnualContribution()).isEqualTo(1_250_000L);

        ArgumentCaptor<ReportRequest> reportCaptor = ArgumentCaptor.forClass(ReportRequest.class);
        verify(reportService).generateReport(reportCaptor.capture());
        ReportRequest reportRequest = reportCaptor.getValue();

        assertThat(reportRequest.getUserProfile().getType()).isEqualTo("STABLE_SEEKING");
        assertThat(reportRequest.getUserProfile().getTotalScore()).isEqualTo(17);
        assertThat(reportRequest.getUserProfile().getGrade()).isEqualTo(2);

        assertThat(reportRequest.getPortfolio()).extracting(
            ReportRequest.PortfolioItem::getCategory, ReportRequest.PortfolioItem::getWeightPercent
        ).containsExactly(
            tuple("연금저축 주식", 20.0),
            tuple("연금저축 채권", 25.0)
        );

        ReportRequest.RetirementPlan plan = reportRequest.getRetirementPlan();
        assertThat(plan.getMonthlyContribution()).isEqualTo(200_000L);
        assertThat(plan.getCurrentAge()).isEqualTo(45);
        assertThat(plan.getTargetAge()).isEqualTo(65);
        assertThat(plan.getExpectedReturnRate()).isEqualTo(0.05);
        assertThat(plan.getTotalContribution()).isEqualTo(48_000_000L);
        assertThat(plan.getExpectedProfit()).isEqualTo(32_350_000L);
        assertThat(plan.getTaxBenefit()).isEqualTo(1_000_000L);
        assertThat(plan.getExpectedAssetAtRetirement()).isEqualTo(120_000_000L);

        ReportRequest.Metrics metrics = reportRequest.getMetrics();
        assertThat(metrics.getAnnualTaxBenefit()).isEqualTo(1_000_000L);
        assertThat(metrics.getAssetAt65()).isEqualTo(120_000_000L);
        assertThat(metrics.getAssetIncreaseAfter20Years()).isEqualTo(40_000_000L);
        assertThat(metrics.getCumulativeTaxBenefit()).isEqualTo(20_000_000L);
    }

    private MydataSnapshotRequest mydata() {
        MydataSnapshotRequest mydata = new MydataSnapshotRequest();
        // bankTransaction.salaryAmt(세후 월급, 245만원×12=2,940만원)와 일부러 다른 값으로 둬서
        // taxSavingAnalysisRequestOf()가 annualGrossSalary를 쓰는지(세후 월급 추정치를 쓰지 않는지) 검증한다.
        mydata.setAnnualGrossSalary(42_000_000L);

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
        pensionSavings.setAnnualContribution(900_000L);

        MydataSnapshotRequest.PersonalPensionAccount irp = new MydataSnapshotRequest.PersonalPensionAccount();
        irp.setAccountType(PensionAccountType.IRP);
        irp.setAccumAmt(2_300_000L);
        irp.setEvalAmt(2_400_000L);
        irp.setEmployerAmt(0L);
        irp.setEmployeeAmt(2_300_000L);
        irp.setIssueDate(LocalDate.parse("2022-06-01"));
        irp.setRcvStartDate(LocalDate.parse("2054-01-01"));
        irp.setAnnualContribution(1_250_000L);

        mydata.setPersonalPensionAccounts(List.of(pensionSavings, irp));

        MydataSnapshotRequest.SavingsInvestment savingsInvestment = new MydataSnapshotRequest.SavingsInvestment();
        MydataSnapshotRequest.SavingsInvestment.Account account = new MydataSnapshotRequest.SavingsInvestment.Account();
        account.setAccountNum("110-123-456789");
        account.setProdName("예금");
        account.setBalanceAmt(20_000_000L);
        savingsInvestment.setAccounts(List.of(account));
        mydata.setSavingsInvestment(savingsInvestment);

        MydataSnapshotRequest.BankTransaction bankTransaction = new MydataSnapshotRequest.BankTransaction();
        bankTransaction.setSalaryAmt(3_400_000L);
        bankTransaction.setExpenseAmt(2_100_000L);
        mydata.setBankTransaction(bankTransaction);

        return mydata;
    }
}
