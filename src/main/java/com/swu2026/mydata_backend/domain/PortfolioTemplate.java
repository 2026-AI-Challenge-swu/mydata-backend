package com.swu2026.mydata_backend.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("portfolio_templates")
public class PortfolioTemplate {
    @Id
    private String id;

    private InvestmentProfileType profileType;
    private String dcDefaultAllocationDescription;
    private List<String> recommendationReasons;
    private List<PortfolioAgeBandAllocation> ageBandAllocations;
}
