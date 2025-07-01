package labcqrssummarize.domain;

import lombok.Data;

@Data
public class PointDeductionRequest {
    private String userId;
    private String ebookId;
    private int price;  // int로 변경, 필수값이라서 null 방지
}
