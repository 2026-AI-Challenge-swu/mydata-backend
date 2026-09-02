package com.swu2026.mydata_backend.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PortfolioRecommendationResponse {

    private String profileType;
    private String dcDefaultAllocationDescription;
    private List<CompositionItem> compositions;
    private List<String> recommendationReasons;
    private double expectedAnnualReturnRate;

    @Getter
    @Builder
    public static class CompositionItem {
        private String category;
        private double weightPercent;
    }
}
