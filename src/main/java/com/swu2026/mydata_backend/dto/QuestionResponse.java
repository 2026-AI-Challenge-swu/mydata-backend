package com.swu2026.mydata_backend.dto;

import com.swu2026.mydata_backend.domain.QuestionDisplayType;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionResponse {

    private String id;
    private String text;
    private String category;
    private int displayOrder;
    private QuestionDisplayType displayType;
    private List<Option> options;

    @Getter
    @Builder
    public static class Option {
        private String text;
        private int order;
    }
}
