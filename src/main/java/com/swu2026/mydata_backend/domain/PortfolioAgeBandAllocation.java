package com.swu2026.mydata_backend.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioAgeBandAllocation {
    private String ageBand;
    private List<PortfolioComposition> maleCompositions;
    private List<PortfolioComposition> femaleCompositions;
}
