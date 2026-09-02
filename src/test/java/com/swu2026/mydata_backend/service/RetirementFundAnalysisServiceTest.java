package com.swu2026.mydata_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.swu2026.mydata_backend.dto.RetirementFundAnalysisResponse;
import org.junit.jupiter.api.Test;

class RetirementFundAnalysisServiceTest {

    private final RetirementFundAnalysisService service = new RetirementFundAnalysisService();

    @Test
    void 예상연금이_목표생활비보다_적으면_부족액과_필요금액을_계산한다() {
        // currentAge=65 → 은퇴까지 남은 기간 0년. 65~84세(20년) 매년 물가상승률을 반영해 합산.
        RetirementFundAnalysisResponse response = service.analyze(2_500_000, 1_590_000, 65);

        assertThat(response.getTargetLivingCost()).isEqualTo(2_500_000);
        assertThat(response.getExpectedMonthlyPension()).isEqualTo(1_590_000);
        assertThat(response.getMonthlyShortfall()).isEqualTo(910_000);
        assertThat(response.getRequiredAmountAtRetirement()).isEqualTo(278_947_661);
        assertThat(response.getInflationRate()).isEqualTo(0.025);
        assertThat(response.getRetirementPayoutYears()).isEqualTo(20);
    }

    @Test
    void 예상연금이_목표생활비_이상이면_부족액과_필요금액은_0이다() {
        RetirementFundAnalysisResponse response = service.analyze(1_000_000, 1_500_000, 65);

        assertThat(response.getMonthlyShortfall()).isZero();
        assertThat(response.getRequiredAmountAtRetirement()).isZero();
    }

    @Test
    void 은퇴까지_기간이_남아있으면_65세부터_20년간_매년_물가상승률을_반영해_필요금액을_계산한다() {
        // currentAge=63 → 65세까지 2년. 65~84세(20년) 각 해마다 (1.025)^(2+t)를 적용해 합산.
        RetirementFundAnalysisResponse response = service.analyze(1_100_000, 1_000_000, 63);

        assertThat(response.getMonthlyShortfall()).isEqualTo(100_000);
        assertThat(response.getRequiredAmountAtRetirement()).isEqualTo(32_205_427);
    }
}
