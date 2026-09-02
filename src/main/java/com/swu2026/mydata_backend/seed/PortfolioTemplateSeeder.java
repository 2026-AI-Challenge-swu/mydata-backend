package com.swu2026.mydata_backend.seed;

import com.swu2026.mydata_backend.domain.InvestmentProfileType;
import com.swu2026.mydata_backend.domain.PortfolioAgeBandAllocation;
import com.swu2026.mydata_backend.domain.PortfolioCategory;
import com.swu2026.mydata_backend.domain.PortfolioComposition;
import com.swu2026.mydata_backend.domain.PortfolioTemplate;
import com.swu2026.mydata_backend.repository.PortfolioTemplateRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// "투자성향 × 연령 × 성별 자산 배분 기준표"를 그대로 옮긴 시드 데이터.
// 각 연령대의 5개 비중(연금저축 주식/채권, IRP 주식/채권/원리금보장)은 항상 합계 100이 되도록 검증된 값.
@Component
@RequiredArgsConstructor
public class PortfolioTemplateSeeder implements CommandLineRunner {

    private final PortfolioTemplateRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        repository.saveAll(List.of(
            stable(),
            stableSeeking(),
            riskNeutral(),
            active(),
            aggressive()
        ));
    }

    private PortfolioTemplate stable() {
        return template(
            InvestmentProfileType.STABLE,
            "원리금보장형 100%",
            List.of(
                "원리금보장형 상품 우선 편입",
                "연금저축은 채권형 상품 중심으로 운용",
                "시장 변동성이 커질 경우 자산배분 재점검"
            ),
            List.of(
                band("20~24", weights(0, 25, 0, 20, 55), weights(5, 25, 0, 15, 55)),
                band("25~29", weights(0, 25, 0, 20, 55), weights(5, 25, 0, 15, 55)),
                bandSame("30~34", weights(0, 20, 0, 20, 60)),
                bandSame("35~39", weights(0, 15, 0, 20, 65))
            )
        );
    }

    private PortfolioTemplate stableSeeking() {
        return template(
            InvestmentProfileType.STABLE_SEEKING,
            "원리금보장 60% + 채권형 40%",
            List.of(
                "연금저축 세액공제 한도 우선 활용",
                "채권형 상품으로 안정성 확보하면서 주식형 상품 일부 편입",
                "연 1회 자산배분 점검"
            ),
            List.of(
                band("20~24", weights(20, 25, 10, 20, 25), weights(25, 25, 15, 20, 15)),
                band("25~29", weights(15, 25, 10, 20, 30), weights(20, 25, 15, 20, 20)),
                band("30~34", weights(10, 25, 10, 25, 30), weights(15, 25, 10, 25, 25)),
                bandSame("35~39", weights(10, 25, 0, 25, 40))
            )
        );
    }

    private PortfolioTemplate riskNeutral() {
        return template(
            InvestmentProfileType.RISK_NEUTRAL,
            "채권형 50% + 주식형 30% + 원리금보장 20%",
            List.of(
                "TDF 등을 활용해 장기적인 자산배분 관리",
                "국내·해외 주식 및 채권 분산",
                "시장 상황에 따라 정기적으로 리밸런싱"
            ),
            List.of(
                band("20~24", weights(35, 20, 20, 15, 10), weights(40, 20, 20, 15, 5)),
                band("25~29", weights(30, 20, 20, 20, 10), weights(35, 20, 20, 20, 5)),
                band("30~34", weights(25, 20, 15, 20, 20), weights(30, 20, 20, 20, 10)),
                band("35~39", weights(20, 20, 15, 25, 20), weights(25, 20, 15, 25, 15))
            )
        );
    }

    private PortfolioTemplate active() {
        return template(
            InvestmentProfileType.ACTIVE,
            "주식형 50% + 채권형 30% + 원리금보장 20%",
            List.of(
                "국내·해외 분산 ETF 중심으로 장기 투자",
                "반기 1회 이상 자산배분 점검",
                "단기 시장 변동성을 감내할 수 있는 경우 적합"
            ),
            List.of(
                band("20~24", weights(45, 10, 30, 10, 5), weights(50, 10, 30, 10, 0)),
                band("25~29", weights(40, 15, 30, 10, 5), weights(45, 15, 30, 10, 0)),
                band("30~34", weights(35, 15, 30, 15, 5), weights(40, 15, 30, 15, 0)),
                band("35~39", weights(30, 15, 25, 20, 10), weights(35, 15, 30, 20, 0))
            )
        );
    }

    private PortfolioTemplate aggressive() {
        return template(
            InvestmentProfileType.AGGRESSIVE,
            "주식형 70% + 채권형 30%",
            List.of(
                "국내·해외 주식형 ETF 중심의 장기 분산투자",
                "분기 1회 포트폴리오 점검",
                "높은 단기 손실 가능성을 감내할 수 있는 투자자에게 적합"
            ),
            List.of(
                band("20~24", weights(55, 0, 35, 5, 5), weights(60, 0, 35, 5, 0)),
                band("25~29", weights(50, 5, 35, 5, 5), weights(55, 5, 35, 5, 0)),
                band("30~34", weights(45, 10, 35, 5, 5), weights(50, 10, 35, 5, 0)),
                band("35~39", weights(40, 10, 35, 10, 5), weights(45, 10, 35, 10, 0))
            )
        );
    }

    private PortfolioTemplate template(
        InvestmentProfileType profileType,
        String dcDefaultAllocationDescription,
        List<String> recommendationReasons,
        List<PortfolioAgeBandAllocation> ageBandAllocations
    ) {
        PortfolioTemplate portfolioTemplate = new PortfolioTemplate();
        portfolioTemplate.setProfileType(profileType);
        portfolioTemplate.setDcDefaultAllocationDescription(dcDefaultAllocationDescription);
        portfolioTemplate.setRecommendationReasons(recommendationReasons);
        portfolioTemplate.setAgeBandAllocations(ageBandAllocations);
        return portfolioTemplate;
    }

    private PortfolioAgeBandAllocation band(
        String ageBand, List<PortfolioComposition> male, List<PortfolioComposition> female
    ) {
        return PortfolioAgeBandAllocation.builder()
            .ageBand(ageBand)
            .maleCompositions(male)
            .femaleCompositions(female)
            .build();
    }

    private PortfolioAgeBandAllocation bandSame(String ageBand, List<PortfolioComposition> compositions) {
        return band(ageBand, compositions, compositions);
    }

    // 순서 고정: 연금저축 주식, 연금저축 채권, IRP 주식, IRP 채권, IRP 원리금보장
    private List<PortfolioComposition> weights(
        double pensionSavingsEquity,
        double pensionSavingsBond,
        double irpEquity,
        double irpBond,
        double irpGuaranteed
    ) {
        return List.of(
            PortfolioComposition.builder()
                .category(PortfolioCategory.PENSION_SAVINGS_EQUITY)
                .weightPercent(pensionSavingsEquity)
                .build(),
            PortfolioComposition.builder()
                .category(PortfolioCategory.PENSION_SAVINGS_BOND)
                .weightPercent(pensionSavingsBond)
                .build(),
            PortfolioComposition.builder()
                .category(PortfolioCategory.IRP_EQUITY)
                .weightPercent(irpEquity)
                .build(),
            PortfolioComposition.builder()
                .category(PortfolioCategory.IRP_BOND)
                .weightPercent(irpBond)
                .build(),
            PortfolioComposition.builder()
                .category(PortfolioCategory.IRP_GUARANTEED)
                .weightPercent(irpGuaranteed)
                .build()
        );
    }
}
