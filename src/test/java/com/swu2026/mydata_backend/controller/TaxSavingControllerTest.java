package com.swu2026.mydata_backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.swu2026.mydata_backend.dto.TaxSavingAnalysisResponse;
import com.swu2026.mydata_backend.service.TaxSavingAnalysisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TaxSavingController.class)
class TaxSavingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaxSavingAnalysisService service;

    @Test
    void 절세효과_분석을_요청하면_200과_공제액_비교_데이터를_반환한다() throws Exception {
        given(service.analyze(any())).willReturn(
            TaxSavingAnalysisResponse.builder()
                .totalSalary(52_000_000)
                .deductionRate(0.165)
                .currentPersonalPensionAnnualContribution(1_075_000)
                .currentRetirementPensionAnnualContribution(640_000)
                .currentEligibleAmount(1_715_000)
                .currentDeductionAmount(282_975)
                .recommendedEligibleAmount(9_000_000)
                .recommendedDeductionAmount(1_485_000)
                .increaseAmount(1_202_025)
                .build()
        );

        String requestBody = """
            {
              "totalSalary": 52000000,
              "personalPensionAccumAmt": 4300000,
              "personalPensionIssueDate": "2022-06-01",
              "retirementPensionBalanceAmt": 3200000,
              "retirementPensionIssueDate": "2021-03-15"
            }
            """;

        mockMvc.perform(post("/api/tax-saving/analysis")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.currentDeductionAmount").value(282_975))
            .andExpect(jsonPath("$.recommendedDeductionAmount").value(1_485_000))
            .andExpect(jsonPath("$.increaseAmount").value(1_202_025));
    }

    @Test
    void 필수값이_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/api/tax-saving/analysis")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }
}
