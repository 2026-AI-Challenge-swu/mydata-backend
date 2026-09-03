package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.dto.FutureAssetSimulationResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FutureAssetSimulationService {

    // RetirementReportService도 이 두 값을 그대로 참조함(2026-09-03: 각자 따로 상수를 갖고 있다가
    // 하나만 바뀌면 조용히 어긋날 위험이 있어서 통합 — package-private으로 공유).
    static final int TARGET_AGE = 65;
    static final long MONTHLY_PLUS_20 = 200_000L;
    private static final long MONTHLY_PLUS_40 = 400_000L;

    public FutureAssetSimulationResponse simulate(int currentAge, long currentTotalAssets, double annualReturnRate) {
        List<FutureAssetSimulationResponse.Point> points = new ArrayList<>();

        long maintainAmount = currentTotalAssets;
        long plus20Amount = currentTotalAssets;
        long plus40Amount = currentTotalAssets;

        points.add(point(currentAge, maintainAmount, plus20Amount, plus40Amount));

        for (int age = currentAge + 1; age <= TARGET_AGE; age++) {
            maintainAmount = grow(maintainAmount, 0, annualReturnRate);
            plus20Amount = grow(plus20Amount, MONTHLY_PLUS_20, annualReturnRate);
            plus40Amount = grow(plus40Amount, MONTHLY_PLUS_40, annualReturnRate);
            points.add(point(age, maintainAmount, plus20Amount, plus40Amount));
        }

        return FutureAssetSimulationResponse.builder()
            .currentAge(currentAge)
            .targetAge(TARGET_AGE)
            .points(points)
            .build();
    }

    private long grow(long currentAmount, long monthlyContribution, double annualReturnRate) {
        return Math.round((currentAmount + monthlyContribution * 12) * (1 + annualReturnRate));
    }

    private FutureAssetSimulationResponse.Point point(int age, long maintainAmount, long plus20Amount, long plus40Amount) {
        return FutureAssetSimulationResponse.Point.builder()
            .age(age)
            .maintainAmount(maintainAmount)
            .plus20Amount(plus20Amount)
            .plus40Amount(plus40Amount)
            .build();
    }
}
