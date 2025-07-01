package labcqrssummarize.domain;

import lombok.Data;

@Data
public class PointDeductionResponse {
    private String userId;
    private String ebookId;
    private int deductedPoint;
    private int remainingPoint;
    private String message = "";
    private boolean success;
}
