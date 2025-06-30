package labcqrssummarize.domain;

import lombok.Data;

@Data
public class PointDeductionRequest {
    private String userId;
    private String ebookId;
    private Integer price;
} 