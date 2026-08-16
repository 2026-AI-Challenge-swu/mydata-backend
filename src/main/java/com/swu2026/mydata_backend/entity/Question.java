package com.swu2026.mydata_backend.entity;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class Question {
    private String text;
    private String category;
    private int displayOrder;

    @Size(max = 5)
    private List<QuestionOption> options;
}
