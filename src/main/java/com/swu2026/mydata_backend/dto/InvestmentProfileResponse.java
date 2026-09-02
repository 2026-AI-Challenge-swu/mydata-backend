package com.swu2026.mydata_backend.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InvestmentProfileResponse {

    private int totalScore;
    private String type;
    private int grade;
    private String emoji;
    private String officialName;
    private String nickname;
    private String description;
    private String cardBackground;
    private String badgeBackground;
    private String accentColor;
    private List<CategoryScore> categoryScores;

    @Getter
    @Builder
    public static class CategoryScore {
        private String label;
        private double percent;
    }
}
