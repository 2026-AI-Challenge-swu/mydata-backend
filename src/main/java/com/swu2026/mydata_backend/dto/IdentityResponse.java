package com.swu2026.mydata_backend.dto;

import com.swu2026.mydata_backend.domain.Gender;
import lombok.Builder;
import lombok.Getter;

// 국민연금(NationalPensionResponse)처럼 실제 API 스펙 문서가 없는 데이터 소스(본인인증)라
// snake_case 변환 없이 domain 형태 그대로 응답.
@Getter
@Builder
public class IdentityResponse {

    private String name;
    private int birthYear;
    private Gender gender;
}
