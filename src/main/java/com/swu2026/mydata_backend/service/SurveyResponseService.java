package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.domain.InvestmentProfileType;
import com.swu2026.mydata_backend.domain.Question;
import com.swu2026.mydata_backend.dto.InvestmentProfileResponse;
import com.swu2026.mydata_backend.dto.SurveyAnswerRequest;
import com.swu2026.mydata_backend.repository.QuestionRepository;
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
