package com.swu2026.mydata_backend.dto;

import java.util.List;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportResponse {

    private String totalComment;
    private List<RoadMapItem> roadMap;
    private List<CounsellingPoint> counsellingPoints;

    @Data
    public static class RoadMapItem {

        private int id;
        private String time;
        private String todo;
    }

    @Data
    public static class CounsellingPoint {

        private String tendency;
        private String detail;
    }
}
