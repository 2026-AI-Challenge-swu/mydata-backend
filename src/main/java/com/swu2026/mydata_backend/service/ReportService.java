package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.dto.ReportRequest;
import com.swu2026.mydata_backend.dto.ReportResponse;
import com.swu2026.mydata_backend.exception.ReportGenerationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final RestClient reportApiRestClient;

    public ReportResponse generateReport(ReportRequest request) {
        try {
            return reportApiRestClient.post()
                .uri("/api/v1/report")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ReportResponse.class);
        } catch (RestClientException e) {
            throw new ReportGenerationException("리포트 생성 API 호출에 실패했습니다.", true);
        }
    }
}
