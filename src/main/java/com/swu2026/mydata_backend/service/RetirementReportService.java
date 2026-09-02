package com.swu2026.mydata_backend.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetirementReportService {

    private static final long DEFAULT_TARGET_LIVING_COST = 2_500_000L;

    private static final int TARGET_AGE = 65;
    private static final long MONTHLY_CONTRIBUTION_FOR_PLAN = 200_000L;

    private final SurveyResponseService surveyResponseService;
    private final AssetPensionStatusService assetPensionStatusService;
    private final PortfolioRecommendationService portfolioRecommendationService;
    private final RetirementFundAnalysisService retirementFundAnalysisService;
    private final FutureAssetSimulationService futureAssetSimulationService;
    private final TaxSavingAnalysisService taxSavingAnalysisService;
    private final ReportService reportService;

    public RetirementReportResponse generate(RetirementReportRequest request) {
        MydataSnapshotRequest mydata = request.getMydata();

        InvestmentProfileResponse investmentProfile = surveyResponseService.submit(surveyAnswerRequestOf(request));
        InvestmentProfileType profileType = InvestmentProfileType.valueOf(investmentProfile.getType());

        AssetPensionStatusResponse assetPensionStatus = assetPensionStatusService.calculate(mydata);

        PortfolioRecommendationResponse recommendedPortfolio = portfolioRecommendationService.recommend(
            profileType, request.getCurrentAge(), request.getGender()
        );

        long targetLivingCost = request.getTargetLivingCost() != null
            ? request.getTargetLivingCost()
            : DEFAULT_TARGET_LIVING_COST;
        RetirementFundAnalysisResponse retirementFundAnalysis = retirementFundAnalysisService.analyze(
            targetLivingCost, assetPensionStatus.getExpectedMonthlyPension(), request.getCurrentAge()
        );

        FutureAssetSimulationResponse futureAssetSimulation = futureAssetSimulationService.simulate(
            request.getCurrentAge(), assetPensionStatus.getTotalAssets(), recommendedPortfolio.getExpectedAnnualReturnRate()
        );

        TaxSavingAnalysisResponse taxSavingAnalysis = taxSavingAnalysisService.analyze(
            taxSavingAnalysisRequestOf(mydata)
        );

        ReportResponse aiReport = reportService.generateReport(
            reportRequestOf(investmentProfile, recommendedPortfolio, futureAssetSimulation, taxSavingAnalysis, request.getCurrentAge())
        );

        return RetirementReportResponse.builder()
            .investmentProfile(investmentProfile)
            .assetPensionStatus(assetPensionStatus)
            .retirementFundAnalysis(retirementFundAnalysis)
            .recommendedPortfolio(recommendedPortfolio)
            .futureAssetSimulation(futureAssetSimulation)
            .taxSavingAnalysis(taxSavingAnalysis)
            .aiReport(aiReport)
            .build();
    }

    private SurveyAnswerRequest surveyAnswerRequestOf(RetirementReportRequest request) {
        SurveyAnswerRequest surveyAnswerRequest = new SurveyAnswerRequest();
        surveyAnswerRequest.setAnswers(request.getSurveyAnswers());
        return surveyAnswerRequest;
    }

    private TaxSavingAnalysisRequest taxSavingAnalysisRequestOf(MydataSnapshotRequest mydata) {
        TaxSavingAnalysisRequest taxSavingAnalysisRequest = new TaxSavingAnalysisRequest();
        taxSavingAnalysisRequest.setTotalSalary(mydata.getAnnualGrossSalary());
        taxSavingAnalysisRequest.setPensionSavingsAnnualContribution(
            annualContributionOf(mydata, PensionAccountType.PENSION_SAVINGS)
        );
        taxSavingAnalysisRequest.setPersonalPensionAnnualContribution(
            annualContributionOf(mydata, PensionAccountType.IRP)
        );
        return taxSavingAnalysisRequest;
    }

    private long annualContributionOf(MydataSnapshotRequest mydata, PensionAccountType accountType) {
        return mydata.getPersonalPensionAccounts().stream()
            .filter(account -> account.getAccountType() == accountType)
            .mapToLong(MydataSnapshotRequest.PersonalPensionAccount::getAnnualContribution)
            .sum();
    }

    private ReportRequest reportRequestOf(
        InvestmentProfileResponse investmentProfile,
        PortfolioRecommendationResponse recommendedPortfolio,
        FutureAssetSimulationResponse futureAssetSimulation,
        TaxSavingAnalysisResponse taxSavingAnalysis,
        int currentAge
    ) {
        ReportRequest reportRequest = new ReportRequest();

        ReportRequest.UserProfile userProfile = new ReportRequest.UserProfile();
        userProfile.setTotalScore(investmentProfile.getTotalScore());
        userProfile.setType(investmentProfile.getType());
        userProfile.setGrade(investmentProfile.getGrade());
        userProfile.setEmoji(investmentProfile.getEmoji());
        userProfile.setOfficialName(investmentProfile.getOfficialName());
        userProfile.setNickname(investmentProfile.getNickname());
        userProfile.setDescription(investmentProfile.getDescription());
        reportRequest.setUserProfile(userProfile);

        reportRequest.setPortfolio(recommendedPortfolio.getCompositions().stream()
            .map(composition -> {
                ReportRequest.PortfolioItem item = new ReportRequest.PortfolioItem();
                item.setCategory(composition.getCategory());
                item.setWeightPercent(composition.getWeightPercent());
                return item;
            })
            .toList());

        long assetAtRetirement = pointAt(futureAssetSimulation, TARGET_AGE).getPlus20Amount();
        int contributionYears = Math.max(0, TARGET_AGE - currentAge);
        long totalContribution = MONTHLY_CONTRIBUTION_FOR_PLAN * 12 * contributionYears;
        long expectedProfit = assetAtRetirement - futureAssetSimulation.getPoints().get(0).getMaintainAmount() - totalContribution;

        ReportRequest.RetirementPlan retirementPlan = new ReportRequest.RetirementPlan();
        retirementPlan.setMonthlyContribution(MONTHLY_CONTRIBUTION_FOR_PLAN);
        retirementPlan.setCurrentAge(currentAge);
        retirementPlan.setTargetAge(TARGET_AGE);
        retirementPlan.setExpectedReturnRate(recommendedPortfolio.getExpectedAnnualReturnRate());
        retirementPlan.setTotalContribution(totalContribution);
        retirementPlan.setExpectedProfit(expectedProfit);
        retirementPlan.setTaxBenefit(taxSavingAnalysis.getIncreaseAmount());
        retirementPlan.setExpectedAssetAtRetirement(assetAtRetirement);
        reportRequest.setRetirementPlan(retirementPlan);

        int after20YearsAge = Math.min(currentAge + 20, TARGET_AGE);
        long assetIncreaseAfter20Years = pointAt(futureAssetSimulation, after20YearsAge).getPlus20Amount()
            - pointAt(futureAssetSimulation, after20YearsAge).getMaintainAmount();

        ReportRequest.Metrics metrics = new ReportRequest.Metrics();
        metrics.setAnnualTaxBenefit(taxSavingAnalysis.getIncreaseAmount());
        metrics.setAssetAt65(assetAtRetirement);
        metrics.setAssetIncreaseAfter20Years(assetIncreaseAfter20Years);
        metrics.setCumulativeTaxBenefit(taxSavingAnalysis.getIncreaseAmount() * contributionYears);
        reportRequest.setMetrics(metrics);

        return reportRequest;
    }

    private FutureAssetSimulationResponse.Point pointAt(FutureAssetSimulationResponse simulation, int age) {
        return simulation.getPoints().stream()
            .filter(point -> point.getAge() == age)
            .findFirst()
            .orElseGet(() -> simulation.getPoints().get(simulation.getPoints().size() - 1));
    }
}
