package com.swu2026.mydata_backend.dto;

import com.swu2026.mydata_backend.domain.Gender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class RetirementReportRequest {

    @NotEmpty
    private List<SurveyAnswerRequest.@Valid Answer> surveyAnswers;

    @NotNull
    private Integer currentAge;

    @NotNull
    private Gender gender;

    // 미입력 시 기본값(월 250만원, "20대 후반 평균 기준") 사용
    private Long targetLivingCost;

    @NotNull
    @Valid
    private MydataSnapshotRequest mydata;
}
