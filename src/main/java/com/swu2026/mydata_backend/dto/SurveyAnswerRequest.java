package com.swu2026.mydata_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class SurveyAnswerRequest {

    @NotEmpty
    private List<@Valid Answer> answers;

    @Data
    public static class Answer {

        @NotBlank
        private String questionId;

        @NotNull
        private Integer selectedOrder;
    }
}
