package com.swu2026.mydata_backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetirementReportResponse {

    private InvestmentProfileResponse investmentProfile;
    private PortfolioRecommendationResponse recommendedPortfolio;
    private FutureAssetSimulationResponse futureAssetSimulation;
    private TaxSavingAnalysisResponse taxSavingAnalysis;
    private ReportResponse aiReport;
}
