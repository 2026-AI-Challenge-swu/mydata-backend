package com.swu2026.mydata_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class ReportRequest {

    @NotNull
    @Valid
    private UserProfile userProfile;

    @NotNull
    @Valid
    private List<@Valid PortfolioItem> portfolio;

    @NotNull
    @Valid
    private RetirementPlan retirementPlan;

    @NotNull
    @Valid
    private Metrics metrics;

    @Data
    public static class UserProfile {

        private int totalScore;

        @NotBlank
        private String type;

        private int grade;
        private String emoji;
        private String officialName;
        private String nickname;
        private String description;
    }

    @Data
    public static class PortfolioItem {

        @NotBlank
        private String category;

        private double weightPercent;
    }

    @Data
    public static class RetirementPlan {

        private long monthlyContribution;
        private int currentAge;
        private int targetAge;
        private double expectedReturnRate;
        private long totalContribution;
        private long expectedProfit;
        private long taxBenefit;
        private long expectedAssetAtRetirement;
    }

    @Data
    public static class Metrics {

        private long annualTaxBenefit;
        private long assetAt65;
        private long assetIncreaseAfter20Years;
        private long cumulativeTaxBenefit;
    }
}
