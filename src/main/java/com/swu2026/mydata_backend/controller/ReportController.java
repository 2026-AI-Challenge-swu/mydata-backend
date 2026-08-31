package com.swu2026.mydata_backend.controller;

import com.swu2026.mydata_backend.dto.ReportRequest;
import com.swu2026.mydata_backend.dto.ReportResponse;
import com.swu2026.mydata_backend.service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ReportResponse generate(@RequestBody @Valid ReportRequest request) {
        return reportService.generateReport(request);
    }
}
