package com.swu2026.mydata_backend.domain;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("questions")
public class Question {
    @Id
    private String id;

    private String text;
    private String category;
    private int displayOrder;

    @Builder.Default
    private QuestionDisplayType displayType = QuestionDisplayType.CHOICE;

    @Size(max = 5)
    private List<QuestionOption> options;
}
