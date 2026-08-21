package com.swu2026.mydata_backend.domain;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("questions")
public class Question {
    @Id
    private String id;

    private String text;
    private String category;
    private int displayOrder;

    @Size(max = 5)
    private List<QuestionOption> options;
}
