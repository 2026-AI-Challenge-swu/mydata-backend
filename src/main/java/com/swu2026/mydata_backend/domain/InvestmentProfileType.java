package com.swu2026.mydata_backend.domain;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum InvestmentProfileType {
    STABLE(
        10, 14, 1, "🐢", "안정형", "조심조심 거북이형",
        "손실보다 안전이 최우선인 당신. 원금 보전을 가장 중요하게 생각하며, 예측 가능하고 "
            + "안정적인 수익을 선호해요. 변동성이 낮은 상품 위주로 차근차근 쌓아가는 스타일이에요.",
        "#F0FBF4", "#D6F5E6", "#1FAB6A"
    ),
    STABLE_SEEKING(
        15, 18, 2, "🌱", "안정추구형", "천천히 쌓는 새싹형",
        "안정을 중시하면서도 조금의 수익은 원하는 타입. 큰 손실은 피하고 싶지만, 물가상승률 "
            + "정도는 이겨내는 수익을 기대해요. 균형 잡힌 포트폴리오로 꾸준히 성장하는 스타일이에요.",
        "#EBF3FF", "#C8E3FF", "#2A78D6"
    ),
    RISK_NEUTRAL(
        19, 22, 3, "🧭", "위험중립형", "균형잡힌 나침반형",
        "안전과 수익, 두 마리 토끼를 현명하게 쫓는 유형. 어느 정도의 손실은 감수하면서도 시장 "
            + "평균 수준의 수익을 목표로 해요. 주식과 채권을 균형 있게 배분하는 게 맞아요.",
        "#F5F0FF", "#E2D5FF", "#7C3AED"
    ),
    ACTIVE(
        23, 26, 4, "🦅", "적극투자형", "과감한 독수리형",
        "시장의 변동성을 기회로 보는 적극적 투자자. 단기 손실도 감수하며 장기 수익을 추구해요. "
            + "주식형 비중을 늘리되, 분산 투자로 리스크를 조절하는 게 중요해요.",
        "#FFF8EC", "#FFE5AA", "#EA8C00"
    ),
    AGGRESSIVE(
        27, 32, 5, "🐆", "공격투자형", "무한질주 호랑이형",
        "최대 수익을 위해 고위험도 감수하는 공격적 투자자. 단기 큰 손실도 버틸 수 있고 투자 "
            + "경험도 풍부해요. 주식·ETF 중심으로 공격적으로 운용하되, 철저한 전략도 함께 챙겨야 해요.",
        "#FEF2F2", "#FFD5D0", "#E85D4A"
    );

    private final int minScore;
    private final int maxScore;
    private final int grade;
    private final String emoji;
    private final String officialName;
    private final String nickname;
    private final String description;
    // Figma "투자성향별결과" 참조 시안(node 58:535) 기준 유형별 컬러 포인트
    private final String cardBackground;
    private final String badgeBackground;
    private final String accentColor;

    InvestmentProfileType(
        int minScore, int maxScore, int grade, String emoji,
        String officialName, String nickname, String description,
        String cardBackground, String badgeBackground, String accentColor
    ) {
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.grade = grade;
        this.emoji = emoji;
        this.officialName = officialName;
        this.nickname = nickname;
        this.description = description;
        this.cardBackground = cardBackground;
        this.badgeBackground = badgeBackground;
        this.accentColor = accentColor;
    }

    public static InvestmentProfileType fromScore(int score) {
        return Arrays.stream(values())
            .filter(type -> score >= type.minScore && score <= type.maxScore)
            .findFirst()
            .orElseGet(() -> score < STABLE.minScore ? STABLE : AGGRESSIVE);
    }
}
