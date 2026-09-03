package com.swu2026.mydata_backend.dto;

import lombok.Builder;
import lombok.Getter;

// 국민연금(NationalPensionResponse)처럼 실제 API 스펙 문서가 없는 데이터 소스(공공 마이데이터
// 소득금액증명원)라 snake_case 변환 없이 domain 형태 그대로 응답.
@Getter
@Builder
public class IncomeResponse {

    private long annualGrossSalary;
}
