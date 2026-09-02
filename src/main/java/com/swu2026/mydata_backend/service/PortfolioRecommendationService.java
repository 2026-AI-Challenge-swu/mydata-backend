package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.domain.Gender;
import com.swu2026.mydata_backend.domain.InvestmentProfileType;
import com.swu2026.mydata_backend.domain.PortfolioAgeBandAllocation;
import com.swu2026.mydata_backend.domain.PortfolioComposition;
import com.swu2026.mydata_backend.domain.PortfolioTemplate;
import com.swu2026.mydata_backend.dto.PortfolioRecommendationResponse;
import com.swu2026.mydata_backend.repository.PortfolioTemplateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PortfolioRecommendationService {

    private final PortfolioTemplateRepository repository;

    public PortfolioRecommendationResponse recommend(InvestmentProfileType profileType, int age, Gender gender) {
        PortfolioTemplate template = repository.findByProfileType(profileType)
            .orElseThrow(() -> new IllegalStateException("포트폴리오 템플릿이 없습니다: " + profileType));

        String ageBand = ageBandOf(age);
        PortfolioAgeBandAllocation allocation = template.getAgeBandAllocations().stream()
            .filter(band -> band.getAgeBand().equals(ageBand))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("연령대 배분 데이터가 없습니다: " + ageBand));

        List<PortfolioComposition> compositions = gender == Gender.MALE
            ? allocation.getMaleCompositions()
            : allocation.getFemaleCompositions();

        double expectedAnnualReturnRate = compositions.stream()
            .mapToDouble(composition ->
                composition.getWeightPercent() / 100.0 * composition.getCategory().getExpectedAnnualReturnRate())
            .sum();

        return PortfolioRecommendationResponse.builder()
            .profileType(profileType.name())
            .dcDefaultAllocationDescription(template.getDcDefaultAllocationDescription())
            .compositions(compositions.stream()
                .map(composition -> PortfolioRecommendationResponse.CompositionItem.builder()
                    .category(composition.getCategory().getLabel())
                    .weightPercent(composition.getWeightPercent())
                    .build())
                .toList())
            .recommendationReasons(template.getRecommendationReasons())
            .expectedAnnualReturnRate(expectedAnnualReturnRate)
            .build();
    }

    private String ageBandOf(int age) {
        if (age < 25) {
            return "20~24";
        }
        if (age < 30) {
            return "25~29";
        }
        if (age < 35) {
            return "30~34";
        }
        return "35~39";
    }
}
