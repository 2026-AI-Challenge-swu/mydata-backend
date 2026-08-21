package com.swu2026.mydata_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.swu2026.mydata_backend.dto.TaxSavingAnalysisRequest;
import com.swu2026.mydata_backend.dto.TaxSavingAnalysisResponse;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class TaxSavingAnalysisServiceTest {

    private final TaxSavingAnalysisService service = new TaxSavingAnalysisService();

    @Test
    void 연간_납입액을_직접_받으면_그대로_사용해_공제액을_계산한다() {
        TaxSavingAnalysisRequest request = request("2022-06-01", "2021-03-15", 52_000_000);
        request.setPersonalPensionAnnualContribution(3_000_000L);
        request.setRetirementPensionAnnualContribution(3_000_000L);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getCurrentPersonalPensionAnnualContribution()).isEqualTo(3_000_000);
        assertThat(response.getCurrentRetirementPensionAnnualContribution()).isEqualTo(3_000_000);
        assertThat(response.getDeductionRate()).isEqualTo(0.165);
        // eligible = min(3,000,000, 6,000,000) + 3,000,000 = 6,000,000
        assertThat(response.getCurrentEligibleAmount()).isEqualTo(6_000_000);
        assertThat(response.getCurrentDeductionAmount()).isEqualTo(990_000);
        assertThat(response.getRecommendedEligibleAmount()).isEqualTo(9_000_000);
        assertThat(response.getRecommendedDeductionAmount()).isEqualTo(1_485_000);
        assertThat(response.getIncreaseAmount()).isEqualTo(495_000);
    }

    @Test
    void 연간_납입액을_받지_않으면_누적액을_가입_경과연수로_나눠_계산한다() {
        LocalDate personalIssueDate = LocalDate.now().minusYears(4);
        LocalDate retirementIssueDate = LocalDate.now().minusYears(5);

        TaxSavingAnalysisRequest request = new TaxSavingAnalysisRequest();
        request.setTotalSalary(52_000_000L);
        request.setPersonalPensionAccumAmt(4_000_000L);
        request.setPersonalPensionIssueDate(personalIssueDate);
        request.setRetirementPensionBalanceAmt(5_000_000L);
        request.setRetirementPensionIssueDate(retirementIssueDate);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getCurrentPersonalPensionAnnualContribution()).isEqualTo(1_000_000);
        assertThat(response.getCurrentRetirementPensionAnnualContribution()).isEqualTo(1_000_000);
    }

    @Test
    void 총급여가_5500만원을_초과하면_공제율_13_2퍼센트를_적용한다() {
        TaxSavingAnalysisRequest request = request("2022-06-01", "2021-03-15", 60_000_000);
        request.setPersonalPensionAnnualContribution(6_000_000L);
        request.setRetirementPensionAnnualContribution(3_000_000L);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getDeductionRate()).isEqualTo(0.132);
        assertThat(response.getCurrentEligibleAmount()).isEqualTo(9_000_000);
        assertThat(response.getCurrentDeductionAmount()).isEqualTo(1_188_000);
    }

    @Test
    void 납입액이_한도를_넘으면_공제_한도까지만_인정한다() {
        TaxSavingAnalysisRequest request = request("2022-06-01", "2021-03-15", 52_000_000);
        // 연금저축 800만원(한도 600만원 초과) + IRP 500만원 -> 합산도 900만원 한도로 잘림
        request.setPersonalPensionAnnualContribution(8_000_000L);
        request.setRetirementPensionAnnualContribution(5_000_000L);

        TaxSavingAnalysisResponse response = service.analyze(request);

        assertThat(response.getCurrentEligibleAmount()).isEqualTo(9_000_000);
        assertThat(response.getCurrentDeductionAmount()).isEqualTo(response.getRecommendedDeductionAmount());
        assertThat(response.getIncreaseAmount()).isEqualTo(0);
    }

    private TaxSavingAnalysisRequest request(String personalIssueDate, String retirementIssueDate, long totalSalary) {
        TaxSavingAnalysisRequest request = new TaxSavingAnalysisRequest();
        request.setTotalSalary(totalSalary);
        request.setPersonalPensionAccumAmt(4_300_000L);
        request.setPersonalPensionIssueDate(LocalDate.parse(personalIssueDate));
        request.setRetirementPensionBalanceAmt(3_200_000L);
        request.setRetirementPensionIssueDate(LocalDate.parse(retirementIssueDate));
        return request;
    }
}
