package com.swu2026.mydata_backend.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FutureAssetSimulationResponse {

    private int currentAge;
    private int targetAge;
    private List<Point> points;

    @Getter
    @Builder
    public static class Point {
        private int age;
        private long maintainAmount;
        private long plus20Amount;
        private long plus40Amount;
    }
}
