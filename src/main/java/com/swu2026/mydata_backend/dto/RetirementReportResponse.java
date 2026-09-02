package com.swu2026.mydata_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetirementReportResponse {

    private InvestmentProfileResponse investmentProfile;
    private AssetPensionStatusResponse assetPensionStatus;
    private RetirementFundAnalysisResponse retirementFundAnalysis;
    private PortfolioRecommendationResponse recommendedPortfolio;
    private FutureAssetSimulationResponse futureAssetSimulation;
    private ReportResponse aiReport;
}
