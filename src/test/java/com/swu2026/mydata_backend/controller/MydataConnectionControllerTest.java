package com.swu2026.mydata_backend.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.swu2026.mydata_backend.dto.NationalPensionResponse;
import com.swu2026.mydata_backend.dto.RetirementPensionResponse;
import com.swu2026.mydata_backend.exception.MydataConnectionException;
import com.swu2026.mydata_backend.service.MydataConnectionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MydataConnectionController.class)
class MydataConnectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MydataConnectionService service;

    @Test
    void 국민연금_조회_성공하면_200과_데이터를_반환한다() throws Exception {
        given(service.getNationalPension("success")).willReturn(
            NationalPensionResponse.builder()
                .estimatedMonthlyAmount(320_000)
                .paymentStartAge(65)
                .contributionYears(4)
                .build()
        );

        mockMvc.perform(get("/api/mydata/national-pension"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estimatedMonthlyAmount").value(320_000))
            .andExpect(jsonPath("$.paymentStartAge").value(65));
    }

    @Test
    void 부분실패_시나리오면_국민연금은_502와_실패메시지를_반환한다() throws Exception {
        given(service.getNationalPension("partialFailure")).willThrow(
            new MydataConnectionException("국민연금공단 연계 실패: 이용기관 등록 심사 미완료", false)
        );

        mockMvc.perform(get("/api/mydata/national-pension").param("scenario", "partialFailure"))
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.message").value("국민연금공단 연계 실패: 이용기관 등록 심사 미완료"))
            .andExpect(jsonPath("$.retryable").value(false));
    }

    @Test
    void 국민연금_재시도는_항상_502를_반환한다() throws Exception {
        willThrow(new MydataConnectionException("국민연금공단 연계 실패: 이용기관 등록 심사 미완료", false))
            .given(service).retryNationalPension();

        mockMvc.perform(post("/api/mydata/national-pension/retry"))
            .andExpect(status().isBadGateway())
            .andExpect(jsonPath("$.retryable").value(false));
    }

    @Test
    void 퇴직연금_재시도는_성공하면_200과_데이터를_반환한다() throws Exception {
        given(service.retryRetirementPension()).willReturn(
            RetirementPensionResponse.builder()
                .balanceAmt(3_200_000)
                .evalAmt(3_200_000)
                .issueDate("2021-03-15")
                .build()
        );

        mockMvc.perform(post("/api/mydata/retirement-pension/retry"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.balance_amt").value(3_200_000));
    }
}
