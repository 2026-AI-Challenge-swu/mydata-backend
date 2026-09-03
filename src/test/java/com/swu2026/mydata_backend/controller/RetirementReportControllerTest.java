package com.swu2026.mydata_backend.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.swu2026.mydata_backend.dto.InvestmentProfileResponse;
import com.swu2026.mydata_backend.dto.RetirementReportResponse;
import com.swu2026.mydata_backend.service.RetirementReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RetirementReportController.class)
class RetirementReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RetirementReportService service;

    @Test
    void 통합_리포트를_요청하면_200과_전체_데이터를_반환한다() throws Exception {
        given(service.generate(any())).willReturn(
            RetirementReportResponse.builder()
                .investmentProfile(InvestmentProfileResponse.builder()
                    .totalScore(17)
                    .type("STABLE_SEEKING")
                    .grade(2)
                    .build())
                .build()
        );

        mockMvc.perform(post("/api/retirement-report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestBody()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.investmentProfile.type").value("STABLE_SEEKING"))
            .andExpect(jsonPath("$.investmentProfile.grade").value(2));
    }

    @Test
    void 필수값이_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/api/retirement-report")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void 설문응답이_비어있으면_400을_반환한다() throws Exception {
        String requestBody = """
            {
              "surveyAnswers": [],
              "currentAge": 29,
              "gender": "FEMALE",
              "mydata": %s
            }
            """.formatted(mydataJson());

        mockMvc.perform(post("/api/retirement-report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    @Test
    void 마이데이터가_없으면_400을_반환한다() throws Exception {
        String requestBody = """
            {
              "surveyAnswers": [{"questionId": "q1", "selectedOrder": 1}],
              "currentAge": 29,
              "gender": "FEMALE"
            }
            """;

        mockMvc.perform(post("/api/retirement-report")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

    private String validRequestBody() {
        return """
            {
              "surveyAnswers": [{"questionId": "q1", "selectedOrder": 1}],
              "currentAge": 29,
              "gender": "FEMALE",
              "mydata": %s
            }
            """.formatted(mydataJson());
    }

    private String mydataJson() {
        return """
            {
              "annualGrossSalary": 34000000,
              "nationalPension": {"estimatedMonthlyAmount": 320000, "paymentStartAge": 65, "contributionYears": 4},
              "retirementPension": {"balanceAmt": 3200000, "evalAmt": 3200000, "issueDate": "2021-03-15"},
              "personalPensionAccounts": [
                {
                  "accountType": "PENSION_SAVINGS",
                  "accumAmt": 2000000, "evalAmt": 2050000, "employerAmt": 0, "employeeAmt": 2000000,
                  "issueDate": "2022-06-01", "annualContribution": 900000
                },
                {
                  "accountType": "IRP",
                  "accumAmt": 2300000, "evalAmt": 2400000, "employerAmt": 0, "employeeAmt": 2300000,
                  "issueDate": "2022-06-01", "rcvStartDate": "2054-01-01", "annualContribution": 1250000
                }
              ],
              "savingsInvestment": {
                "accounts": [{"accountNum": "110-123-456789", "prodName": "예금", "balanceAmt": 20000000}]
              },
              "bankTransaction": {"salaryAmt": 3400000, "expenseAmt": 2100000}
            }
            """;
    }
}
