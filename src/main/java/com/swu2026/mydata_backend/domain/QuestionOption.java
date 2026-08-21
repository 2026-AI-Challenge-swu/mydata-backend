package com.swu2026.mydata_backend.domain;

import lombok.Data;

@Data
public class QuestionOption {
    private String text;
    private int order;
    private int score;
}
