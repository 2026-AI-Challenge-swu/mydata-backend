package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.domain.InvestmentProfileType;
import com.swu2026.mydata_backend.domain.Question;
import com.swu2026.mydata_backend.dto.InvestmentProfileResponse;
import com.swu2026.mydata_backend.dto.SurveyAnswerRequest;
import com.swu2026.mydata_backend.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SurveyResponseService {

    private final QuestionRepository repository;

    public InvestmentProfileResponse submit(SurveyAnswerRequest request) {
        int totalScore = request.getAnswers().stream()
            .mapToInt(this::scoreOf)
            .sum();

        InvestmentProfileType type = InvestmentProfileType.fromScore(totalScore);

        return InvestmentProfileResponse.builder()
            .totalScore(totalScore)
            .type(type.name())
            .grade(type.getGrade())
            .emoji(type.getEmoji())
            .officialName(type.getOfficialName())
            .nickname(type.getNickname())
            .description(type.getDescription())
            .cardBackground(type.getCardBackground())
            .badgeBackground(type.getBadgeBackground())
            .accentColor(type.getAccentColor())
            .categoryScores(categoryScoresOf(type))
            .build();
    }

    // 투자성향 점수 카드의 5개 세부 항목 %는 응답별로 계산하지 않고 유형(등급)별 고정값을 그대로 노출한다.
    private List<InvestmentProfileResponse.CategoryScore> categoryScoresOf(InvestmentProfileType type) {
        return List.of(
            categoryScore("투자 경험", type.getInvestmentExperiencePercent()),
            categoryScore("손실 감내도", type.getLossTolerancePercent()),
            categoryScore("투자 기간", type.getInvestmentPeriodPercent()),
            categoryScore("수익 추구도", type.getProfitSeekingPercent()),
            categoryScore("소득 안정성", type.getIncomeStabilityPercent())
        );
    }

    private InvestmentProfileResponse.CategoryScore categoryScore(String label, double percent) {
        return InvestmentProfileResponse.CategoryScore.builder()
            .label(label)
            .percent(percent)
            .build();
    }

    private int scoreOf(SurveyAnswerRequest.Answer answer) {
        Question question = repository.findById(answer.getQuestionId())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "존재하지 않는 questionId: " + answer.getQuestionId()
            ));

        return question.getOptions().stream()
            .filter(option -> option.getOrder() == answer.getSelectedOrder())
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "questionId " + answer.getQuestionId() + "에 없는 selectedOrder: " + answer.getSelectedOrder()
            ))
            .getScore();
    }
}
