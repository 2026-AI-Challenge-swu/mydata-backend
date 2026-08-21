package com.swu2026.mydata_backend.controller;

import com.swu2026.mydata_backend.dto.TaxSavingAnalysisRequest;
import com.swu2026.mydata_backend.dto.TaxSavingAnalysisResponse;
import com.swu2026.mydata_backend.service.TaxSavingAnalysisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tax-saving")
@RequiredArgsConstructor
public class TaxSavingController {

    private final TaxSavingAnalysisService service;

    @PostMapping("/analysis")
    public TaxSavingAnalysisResponse analyze(@RequestBody @Valid TaxSavingAnalysisRequest request) {
        return service.analyze(request);
    }
}
