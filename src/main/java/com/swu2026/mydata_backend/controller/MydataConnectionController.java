package com.swu2026.mydata_backend.controller;

import com.swu2026.mydata_backend.dto.BankTransactionResponse;
import com.swu2026.mydata_backend.dto.NationalPensionResponse;
import com.swu2026.mydata_backend.dto.PersonalPensionResponse;
import com.swu2026.mydata_backend.dto.RetirementPensionResponse;
import com.swu2026.mydata_backend.dto.SavingsInvestmentResponse;
import com.swu2026.mydata_backend.service.MydataConnectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/mydata")
@RequiredArgsConstructor
public class MydataConnectionController {

    private final MydataConnectionService service;

    @GetMapping("/national-pension")
    public NationalPensionResponse getNationalPension(
        @RequestParam(defaultValue = "success") String scenario
    ) {
        return service.getNationalPension(scenario);
    }

    @GetMapping("/retirement-pension")
    public RetirementPensionResponse getRetirementPension(
        @RequestParam(defaultValue = "success") String scenario
    ) {
        return service.getRetirementPension(scenario);
    }

    @GetMapping("/personal-pension")
    public PersonalPensionResponse getPersonalPension(
        @RequestParam(defaultValue = "success") String scenario
    ) {
        return service.getPersonalPension(scenario);
    }

    @GetMapping("/savings-investment")
    public SavingsInvestmentResponse getSavingsInvestment(
        @RequestParam(defaultValue = "success") String scenario
    ) {
        return service.getSavingsInvestment(scenario);
    }

    @GetMapping("/bank-transaction")
    public BankTransactionResponse getBankTransaction(
        @RequestParam(defaultValue = "success") String scenario
    ) {
        return service.getBankTransaction(scenario);
    }

    @PostMapping("/{category}/retry")
    public Object retry(@PathVariable String category) {
        return switch (category) {
            case "national-pension" -> service.retryNationalPension();
            case "retirement-pension" -> service.retryRetirementPension();
            case "personal-pension" -> service.retryPersonalPension();
            case "savings-investment" -> service.retrySavingsInvestment();
            case "bank-transaction" -> service.retryBankTransaction();
            default -> throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "알 수 없는 category: " + category
            );
        };
    }
}
