package com.swu2026.mydata_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.swu2026.mydata_backend.dto.FutureAssetSimulationResponse;
import org.junit.jupiter.api.Test;

class FutureAssetSimulationServiceTest {

    private final FutureAssetSimulationService service = new FutureAssetSimulationService();

    @Test
    void 매년_세_시나리오를_복리로_계산해_65세까지_포인트를_생성한다() {
        FutureAssetSimulationResponse response = service.simulate(63, 10_000_000, 0.10);

        assertThat(response.getCurrentAge()).isEqualTo(63);
        assertThat(response.getTargetAge()).isEqualTo(65);
        assertThat(response.getPoints()).extracting(
            FutureAssetSimulationResponse.Point::getAge,
            FutureAssetSimulationResponse.Point::getMaintainAmount,
            FutureAssetSimulationResponse.Point::getPlus20Amount,
            FutureAssetSimulationResponse.Point::getPlus40Amount
        ).containsExactly(
            tuple(63, 10_000_000L, 10_000_000L, 10_000_000L),
            tuple(64, 11_000_000L, 13_640_000L, 16_280_000L),
            tuple(65, 12_100_000L, 17_644_000L, 23_188_000L)
        );
    }

    @Test
    void 현재나이가_목표나이와_같으면_시작_포인트_하나만_반환한다() {
        FutureAssetSimulationResponse response = service.simulate(65, 5_000_000, 0.05);

        assertThat(response.getPoints()).extracting(
            FutureAssetSimulationResponse.Point::getAge,
            FutureAssetSimulationResponse.Point::getMaintainAmount
        ).containsExactly(tuple(65, 5_000_000L));
    }
}
