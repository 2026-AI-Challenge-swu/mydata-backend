package com.swu2026.mydata_backend.controller;

import com.swu2026.mydata_backend.dto.RetirementReportRequest;
import com.swu2026.mydata_backend.dto.RetirementReportResponse;
import com.swu2026.mydata_backend.service.RetirementReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/retirement-report")
@RequiredArgsConstructor
public class RetirementReportController {

    private final RetirementReportService retirementReportService;

    @PostMapping
    public RetirementReportResponse generate(@RequestBody @Valid RetirementReportRequest request) {
        return retirementReportService.generate(request);
    }
}
