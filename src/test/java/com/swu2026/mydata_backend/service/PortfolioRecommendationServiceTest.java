package com.swu2026.mydata_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.swu2026.mydata_backend.domain.Gender;
import com.swu2026.mydata_backend.domain.InvestmentProfileType;
import com.swu2026.mydata_backend.domain.PortfolioAgeBandAllocation;
import com.swu2026.mydata_backend.domain.PortfolioCategory;
import com.swu2026.mydata_backend.domain.PortfolioComposition;
import com.swu2026.mydata_backend.domain.PortfolioTemplate;
import com.swu2026.mydata_backend.dto.PortfolioRecommendationResponse;
import com.swu2026.mydata_backend.repository.PortfolioTemplateRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PortfolioRecommendationServiceTest {

    @Mock
    private PortfolioTemplateRepository repository;

    @InjectMocks
    private PortfolioRecommendationService service;

    @BeforeEach
    void setUp() {
        given(repository.findByProfileType(any())).willReturn(Optional.of(template()));
    }

    @Test
    void 남성_20에서24세는_연금저축주식_100퍼센트다() {
        PortfolioRecommendationResponse response = service.recommend(InvestmentProfileType.STABLE_SEEKING, 22, Gender.MALE);

        assertThat(response.getCompositions()).extracting(
            PortfolioRecommendationResponse.CompositionItem::getCategory,
            PortfolioRecommendationResponse.CompositionItem::getWeightPercent
        ).containsExactly(tuple("연금저축 주식", 100.0));
        assertThat(response.getExpectedAnnualReturnRate()).isEqualTo(0.07);
        assertThat(response.getDcDefaultAllocationDescription()).isEqualTo("DC 기본안");
        assertThat(response.getRecommendationReasons()).containsExactly("문구1", "문구2");
    }

    @Test
    void 여성_20에서24세는_남성과_다른_구성을_가진다() {
        PortfolioRecommendationResponse response = service.recommend(InvestmentProfileType.STABLE_SEEKING, 22, Gender.FEMALE);

        assertThat(response.getCompositions()).extracting(
            PortfolioRecommendationResponse.CompositionItem::getCategory,
            PortfolioRecommendationResponse.CompositionItem::getWeightPercent
        ).containsExactly(tuple("IRP 원리금보장", 100.0));
        assertThat(response.getExpectedAnnualReturnRate()).isEqualTo(0.02);
    }

    @Test
    void 스무살_미만은_20에서24세_밴드로_근사한다() {
        PortfolioRecommendationResponse response = service.recommend(InvestmentProfileType.STABLE_SEEKING, 15, Gender.MALE);

        assertThat(response.getCompositions()).extracting(PortfolioRecommendationResponse.CompositionItem::getCategory)
            .containsExactly("연금저축 주식");
    }

    @Test
    void 마흔살_이상은_35에서39세_밴드로_근사하고_가중평균수익률을_계산한다() {
        PortfolioRecommendationResponse response = service.recommend(InvestmentProfileType.STABLE_SEEKING, 45, Gender.MALE);

        assertThat(response.getCompositions()).extracting(
            PortfolioRecommendationResponse.CompositionItem::getCategory,
            PortfolioRecommendationResponse.CompositionItem::getWeightPercent
        ).containsExactly(
            tuple("연금저축 주식", 50.0),
            tuple("연금저축 채권", 50.0)
        );
        // 0.5 * 0.07(주식) + 0.5 * 0.035(채권) = 0.0525
        assertThat(response.getExpectedAnnualReturnRate()).isCloseTo(0.0525, within(1e-9));
    }

    private PortfolioTemplate template() {
        PortfolioTemplate template = new PortfolioTemplate();
        template.setProfileType(InvestmentProfileType.STABLE_SEEKING);
        template.setDcDefaultAllocationDescription("DC 기본안");
        template.setRecommendationReasons(List.of("문구1", "문구2"));
        template.setAgeBandAllocations(List.of(
            band("20~24",
                compositions(PortfolioCategory.PENSION_SAVINGS_EQUITY, 100),
                compositions(PortfolioCategory.IRP_GUARANTEED, 100)
            ),
            band("25~29",
                compositions(PortfolioCategory.PENSION_SAVINGS_BOND, 100),
                compositions(PortfolioCategory.PENSION_SAVINGS_BOND, 100)
            ),
            band("30~34",
                compositions(PortfolioCategory.IRP_GUARANTEED, 100),
                compositions(PortfolioCategory.IRP_GUARANTEED, 100)
            ),
            band("35~39",
                List.of(
                    PortfolioComposition.builder().category(PortfolioCategory.PENSION_SAVINGS_EQUITY).weightPercent(50).build(),
                    PortfolioComposition.builder().category(PortfolioCategory.PENSION_SAVINGS_BOND).weightPercent(50).build()
                ),
                List.of(
                    PortfolioComposition.builder().category(PortfolioCategory.PENSION_SAVINGS_EQUITY).weightPercent(50).build(),
                    PortfolioComposition.builder().category(PortfolioCategory.PENSION_SAVINGS_BOND).weightPercent(50).build()
                )
            )
        ));
        return template;
    }

    private PortfolioAgeBandAllocation band(String ageBand, List<PortfolioComposition> male, List<PortfolioComposition> female) {
        return PortfolioAgeBandAllocation.builder()
            .ageBand(ageBand)
            .maleCompositions(male)
            .femaleCompositions(female)
            .build();
    }

    private List<PortfolioComposition> compositions(PortfolioCategory category, double weightPercent) {
        return List.of(PortfolioComposition.builder().category(category).weightPercent(weightPercent).build());
    }
}
