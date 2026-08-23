package com.swu2026.mydata_backend.dto;

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
}
