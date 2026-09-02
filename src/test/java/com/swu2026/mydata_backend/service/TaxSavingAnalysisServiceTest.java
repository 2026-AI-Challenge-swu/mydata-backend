package com.swu2026.mydata_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.swu2026.mydata_backend.dto.TaxSavingAnalysisRequest;
import com.swu2026.mydata_backend.dto.TaxSavingAnalysisResponse;
import org.junit.jupiter.api.Test;

class TaxSavingAnalysisServiceTest {

    private final TaxSavingAnalysisService service = new TaxSavingAnalysisService();

    @Test
    void 연간_납입액을_받아_그대로_공제액을_계산한다() {
        TaxSavingAnalysisRequest request = request(52_000_000, 1_000_000, 2_000_000);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getCurrentPensionSavingsAnnualContribution()).isEqualTo(1_000_000);
        assertThat(response.getCurrentPersonalPensionAnnualContribution()).isEqualTo(2_000_000);
        assertThat(response.getDeductionRate()).isEqualTo(0.165);
        assertThat(response.getCurrentEligibleAmount()).isEqualTo(3_000_000);
        assertThat(response.getCurrentDeductionAmount()).isEqualTo(495_000);
        assertThat(response.getRecommendedEligibleAmount()).isEqualTo(9_000_000);
        assertThat(response.getRecommendedDeductionAmount()).isEqualTo(1_485_000);
        assertThat(response.getIncreaseAmount()).isEqualTo(990_000);
    }

    @Test
    void 연봉_5천만원에_개인연금과_IRP를_각각_연_80만원씩_납입하면_절세효과를_계산한다() {
        // 개인연금(연금저축) 80만원 + IRP 80만원 = 연 160만원(세액공제 대상 합산액)
        TaxSavingAnalysisRequest request = request(50_000_000, 800_000, 800_000);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getCurrentPensionSavingsAnnualContribution()).isEqualTo(800_000);
        assertThat(response.getCurrentPersonalPensionAnnualContribution()).isEqualTo(800_000);
        assertThat(response.getDeductionRate()).isEqualTo(0.165);
        assertThat(response.getCurrentEligibleAmount()).isEqualTo(1_600_000);
        assertThat(response.getCurrentDeductionAmount()).isEqualTo(264_000);
        assertThat(response.getRecommendedEligibleAmount()).isEqualTo(9_000_000);
        assertThat(response.getRecommendedDeductionAmount()).isEqualTo(1_485_000);
        assertThat(response.getIncreaseAmount()).isEqualTo(1_221_000);
    }

    @Test
    void 총급여가_5500만원을_초과하면_공제율_13_2퍼센트를_적용한다() {
        TaxSavingAnalysisRequest request = request(60_000_000, 0, 9_000_000);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getDeductionRate()).isEqualTo(0.132);
        assertThat(response.getCurrentEligibleAmount()).isEqualTo(9_000_000);
        assertThat(response.getCurrentDeductionAmount()).isEqualTo(1_188_000);
    }

    @Test
    void 연금저축은_단독으로_600만원까지만_인정한다() {
        // 연금저축에만 900만원을 넣어도 단독 한도(600만원)까지만 인정
        TaxSavingAnalysisRequest request = request(52_000_000, 9_000_000, 0);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getCurrentEligibleAmount()).isEqualTo(6_000_000);
        assertThat(response.getCurrentDeductionAmount()).isEqualTo(990_000);
    }

    @Test
    void 합산_납입액이_한도를_넘으면_공제_한도까지만_인정한다() {
        // 연금저축 600만원(단독 한도까지 인정) + IRP 600만원 = 합산 900만원 한도까지만 인정
        TaxSavingAnalysisRequest request = request(52_000_000, 6_000_000, 6_000_000);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getCurrentEligibleAmount()).isEqualTo(9_000_000);
        assertThat(response.getCurrentDeductionAmount()).isEqualTo(response.getRecommendedDeductionAmount());
        assertThat(response.getIncreaseAmount()).isEqualTo(0);
    }

    private TaxSavingAnalysisRequest request(long totalSalary, long pensionSavingsAnnual, long personalPensionAnnual) {
        TaxSavingAnalysisRequest request = new TaxSavingAnalysisRequest();
        request.setTotalSalary(totalSalary);
        request.setPensionSavingsAnnualContribution(pensionSavingsAnnual);
        request.setPersonalPensionAnnualContribution(personalPensionAnnual);
        return request;
    }
}
