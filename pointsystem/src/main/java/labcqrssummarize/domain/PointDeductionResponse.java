package labcqrssummarize.domain;

import lombok.Data;

@Data
public class PointDeductionResponse {
    private String userId;
    private String ebookId;
    private Integer deductedPoint;
    private Integer remainingPoint;
    private String message;
    private boolean success;
} 