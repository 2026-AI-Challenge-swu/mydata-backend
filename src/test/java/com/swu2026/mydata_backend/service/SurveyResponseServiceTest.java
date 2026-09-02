package com.swu2026.mydata_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.BDDMockito.given;

import com.swu2026.mydata_backend.domain.Question;
import com.swu2026.mydata_backend.domain.QuestionOption;
import com.swu2026.mydata_backend.dto.InvestmentProfileResponse;
import com.swu2026.mydata_backend.dto.SurveyAnswerRequest;
import com.swu2026.mydata_backend.repository.QuestionRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class SurveyResponseServiceTest {

    @Mock
    private QuestionRepository repository;

    @InjectMocks
    private SurveyResponseService service;

    @Test
    void 답변_점수를_합산해_총점과_등급을_계산하고_유형별_고정_세부항목을_반환한다() {
        given(repository.findById("q1")).willReturn(Optional.of(question("q1", 10)));
        given(repository.findById("q2")).willReturn(Optional.of(question("q2", 5)));

        InvestmentProfileResponse response = service.submit(requestOf(
            answer("q1", 1), answer("q2", 1)
        ));

        assertThat(response.getTotalScore()).isEqualTo(15);
        assertThat(response.getType()).isEqualTo("STABLE_SEEKING");
        assertThat(response.getGrade()).isEqualTo(2);

        List<InvestmentProfileResponse.CategoryScore> categoryScores = response.getCategoryScores();
        assertThat(categoryScores).extracting(
            InvestmentProfileResponse.CategoryScore::getLabel,
            InvestmentProfileResponse.CategoryScore::getPercent
        ).containsExactly(
            tuple("투자 경험", 20.0),
            tuple("손실 감내도", 20.0),
            tuple("투자 기간", 40.0),
            tuple("수익 추구도", 35.0),
            tuple("소득 안정성", 70.0)
        );
    }

    @Test
    void 존재하지_않는_questionId면_400을_던진다() {
        given(repository.findById("unknown")).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.submit(requestOf(answer("unknown", 1))))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value()).isEqualTo(400));
    }

    @Test
    void 문항에_없는_selectedOrder면_400을_던진다() {
        given(repository.findById("q1")).willReturn(Optional.of(question("q1", 10)));

        assertThatThrownBy(() -> service.submit(requestOf(answer("q1", 99))))
            .isInstanceOf(ResponseStatusException.class)
            .satisfies(ex -> assertThat(((ResponseStatusException) ex).getStatusCode().value()).isEqualTo(400));
    }

    private SurveyAnswerRequest requestOf(SurveyAnswerRequest.Answer... answers) {
        SurveyAnswerRequest request = new SurveyAnswerRequest();
        request.setAnswers(List.of(answers));
        return request;
    }

    private SurveyAnswerRequest.Answer answer(String questionId, int selectedOrder) {
        SurveyAnswerRequest.Answer answer = new SurveyAnswerRequest.Answer();
        answer.setQuestionId(questionId);
        answer.setSelectedOrder(selectedOrder);
        return answer;
    }

    private Question question(String id, int score) {
        return Question.builder()
            .id(id)
            .text("문항")
            .category("카테고리")
            .displayOrder(1)
            .options(List.of(
                QuestionOption.builder().text("보기").order(1).score(score).build()
            ))
            .build();
    }
}
