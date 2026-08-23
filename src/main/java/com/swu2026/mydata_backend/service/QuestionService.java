package com.swu2026.mydata_backend.service;

import com.swu2026.mydata_backend.domain.Question;
import com.swu2026.mydata_backend.dto.QuestionResponse;
import com.swu2026.mydata_backend.repository.QuestionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository repository;

    public List<QuestionResponse> getQuestions() {
        return repository.findAllByOrderByDisplayOrderAsc().stream()
            .map(this::toQuestionResponse)
            .toList();
    }

    private QuestionResponse toQuestionResponse(Question question) {
        List<QuestionResponse.Option> options = question.getOptions().stream()
            .map(option -> QuestionResponse.Option.builder()
                .text(option.getText())
                .order(option.getOrder())
                .build())
            .toList();

        return QuestionResponse.builder()
            .id(question.getId())
            .text(question.getText())
            .category(question.getCategory())
            .displayOrder(question.getDisplayOrder())
            .displayType(question.getDisplayType())
            .options(options)
            .build();
    }
}
