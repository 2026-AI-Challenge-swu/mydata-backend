package com.swu2026.mydata_backend.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class InvestmentProfileTypeTest {

    @Test
    void 안정형은_고정된_세부항목_퍼센트를_가진다() {
        assertCategoryPercents(InvestmentProfileType.STABLE, 20, 25, 25, 33, 100);
    }

    @Test
    void 안정추구형은_고정된_세부항목_퍼센트를_가진다() {
        assertCategoryPercents(InvestmentProfileType.STABLE_SEEKING, 20, 20, 40, 35, 70);
    }

    @Test
    void 위험중립형은_고정된_세부항목_퍼센트를_가진다() {
        assertCategoryPercents(InvestmentProfileType.RISK_NEUTRAL, 60, 75, 75, 67, 67);
    }

    @Test
    void 적극투자형은_고정된_세부항목_퍼센트를_가진다() {
        assertCategoryPercents(InvestmentProfileType.ACTIVE, 80, 75, 75, 100, 33);
    }

    @Test
    void 공격투자형은_고정된_세부항목_퍼센트를_가진다() {
        assertCategoryPercents(InvestmentProfileType.AGGRESSIVE, 100, 100, 100, 100, 67);
    }

    private void assertCategoryPercents(
        InvestmentProfileType type,
        double investmentExperience,
        double lossTolerance,
        double investmentPeriod,
        double profitSeeking,
        double incomeStability
    ) {
        assertThat(type.getInvestmentExperiencePercent()).isEqualTo(investmentExperience);
        assertThat(type.getLossTolerancePercent()).isEqualTo(lossTolerance);
        assertThat(type.getInvestmentPeriodPercent()).isEqualTo(investmentPeriod);
        assertThat(type.getProfitSeekingPercent()).isEqualTo(profitSeeking);
        assertThat(type.getIncomeStabilityPercent()).isEqualTo(incomeStability);
    }
}
