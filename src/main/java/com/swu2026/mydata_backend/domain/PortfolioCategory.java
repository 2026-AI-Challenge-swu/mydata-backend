package com.swu2026.mydata_backend.domain;

import lombok.Getter;

@Getter
public enum PortfolioCategory {
    PENSION_SAVINGS_EQUITY("연금저축 주식", 0.07),
    PENSION_SAVINGS_BOND("연금저축 채권", 0.035),
    IRP_EQUITY("IRP 주식", 0.07),
    IRP_BOND("IRP 채권", 0.035),
    IRP_GUARANTEED("IRP 원리금보장", 0.02);

    private final String label;
    private final double expectedAnnualReturnRate;

    PortfolioCategory(String label, double expectedAnnualReturnRate) {
        this.label = label;
        this.expectedAnnualReturnRate = expectedAnnualReturnRate;
    }
}

