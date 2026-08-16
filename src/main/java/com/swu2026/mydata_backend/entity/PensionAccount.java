package com.swu2026.mydata_backend.entity;

import lombok.Data;

@Data
public class PensionAccount {
    private PensionAccountType accountType;
    private String institutionName;
    private long currentBalance;
    private long annualContribution;
}
