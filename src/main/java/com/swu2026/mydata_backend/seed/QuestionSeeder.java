package com.swu2026.mydata_backend.seed;

import com.swu2026.mydata_backend.domain.Question;
import com.swu2026.mydata_backend.domain.QuestionDisplayType;
import com.swu2026.mydata_backend.domain.QuestionOption;
import com.swu2026.mydata_backend.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionSeeder implements CommandLineRunner {

    private final QuestionRepository repository;

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        repository.saveAll(List.of(
            question(1, "지금까지 투자해보신 상품 중에 가장 과감했던 건 무엇인가요?", "투자 경험",
                option("🏦 아직 없어요, 예적금만 해봤어요", 1, 1),
                option("📜 채권이나 채권형 상품", 2, 2),
                option("📊 혼합형 펀드", 3, 3),
                option("📈 주식형 펀드나 ETF", 4, 4),
                option("🎰 코인, 선물·옵션 등", 5, 5)
            ),
            question(2, "넣은 돈이 얼마까지 줄어들어도 버틸 수 있을 것 같나요?", "손실 감내(변동성)", QuestionDisplayType.GAUGE,
                option("🔴 10%", 1, 1),
                option("🟠 30%", 2, 2),
                option("🟡 50%", 3, 3),
                option("🟢 50%+", 4, 4)
            ),
            question(3, "이 돈, 최소 언제까지는 건드리지 않을 수 있나요?", "투자 기간",
                option("📅 1년 미만", 1, 1),
                option("🗓️ 1~3년 정도", 2, 2),
                option("📅 3~5년", 3, 3),
                option("⏳ 5년 이상", 4, 4)
            ),
            question(4, "지금 나에게 더 중요한 건 어느 쪽인가요?", "수익 추구 성향",
                option("🛡️ 원금 지키는 게 최우선이에요", 1, 1),
                option("⚖️ 원금도 지키고 수익도 좀 원해요", 2, 2),
                option("🚀 손실을 감수해도 수익이 더 중요해요", 3, 3)
            ),
            question(5, "펀드, ETF 같은 상품에 대해 어느 정도 알고 계세요?", "금융 이해도",
                option("🤷 솔직히 잘 모르겠어요", 1, 1),
                option("🙂 대충은 알아요", 2, 2),
                option("😎 꽤 잘 알아요", 3, 3)
            ),
            question(6, "수입이 어느 정도 안정적인 편인가요?", "소득 안정성",
                option("📉 매달 들쭉날쭉해요", 1, 1),
                option("📊 대체로 안정적이에요", 2, 2),
                option("📈 매우 안정적이에요", 3, 3)
            ),
            question(7, "선물, 옵션처럼 복잡한 상품을 거래해보신 적 있으세요?", "투자 경험(심화)",
                option("🙅 없어요", 1, 1),
                option("🤏 조금 있어요", 2, 2),
                option("💪 꽤 있어요", 3, 3)
            ),
            question(8, "월급을 받으면 제일 먼저 하시는 일이 뭔가요?", "저축 습관",
                option("⚡ 자동이체로 저축부터 빼요", 1, 1),
                option("📋 이번달 계획을 세우고 써요", 2, 2),
                option("🛍️ 일단 쓰고 남으면 저축해요", 3, 3)
            ),
            question(9, "새로운 연금·투자 상품을 보면 어떻게 하시나요?", "태도(A/B형)", QuestionDisplayType.BINARY,
                option("조건을 꼼꼼히 따져보고 결정해요", 1, 1),
                option("일단 해보고 나중에 판단해요", 2, 2)
            ),
            question(10, "노후 준비를 시작하게 된 계기가 있으신가요?", "동기(참고용)", QuestionDisplayType.BINARY,
                option("국민연금만으론 부족하다는 걸 알게 됐어요", 1, 1),
                option("주변에서 하는 걸 보고 저도 해야겠다 싶었어요", 2, 2)
            )
        ));
    }

    private Question question(int displayOrder, String text, String category, QuestionOption... options) {
        return question(displayOrder, text, category, QuestionDisplayType.CHOICE, options);
    }

    private Question question(
        int displayOrder, String text, String category, QuestionDisplayType displayType, QuestionOption... options
    ) {
        return Question.builder()
            .text(text)
            .category(category)
            .displayOrder(displayOrder)
            .displayType(displayType)
            .options(List.of(options))
            .build();
    }

    private QuestionOption option(String text, int order, int score) {
        return QuestionOption.builder()
            .text(text)
            .order(order)
            .score(score)
            .build();
    }
}
