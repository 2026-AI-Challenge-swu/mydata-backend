package com.swu2026.mydata_backend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("pension_products")
public class PensionProduct {
    @Id
    private String id;

    private String name;
    private PensionAccountType accountType;
    private AssetClass assetClass;
    private int riskLevel;
    private String managementCompany;
    private double expectedReturnRate;
    private String description;
}
