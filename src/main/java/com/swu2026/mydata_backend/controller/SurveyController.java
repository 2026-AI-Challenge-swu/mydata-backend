package com.swu2026.mydata_backend.controller;

import com.swu2026.mydata_backend.dto.InvestmentProfileResponse;
import com.swu2026.mydata_backend.dto.QuestionResponse;
import com.swu2026.mydata_backend.dto.SurveyAnswerRequest;
import com.swu2026.mydata_backend.service.QuestionService;
import com.swu2026.mydata_backend.service.SurveyResponseService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/survey")
@RequiredArgsConstructor
public class SurveyController {

    private final QuestionService questionService;
    private final SurveyResponseService surveyResponseService;

    @GetMapping("/questions")
    public List<QuestionResponse> getQuestions() {
        return questionService.getQuestions();
    }

    @PostMapping("/responses")
    public InvestmentProfileResponse submitResponses(@RequestBody @Valid SurveyAnswerRequest request) {
        return surveyResponseService.submit(request);
    }
}
