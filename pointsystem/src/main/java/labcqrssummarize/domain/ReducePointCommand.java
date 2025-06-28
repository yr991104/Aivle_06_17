package labcqrssummarize.domain;

import lombok.Data;

@Data
public class ReducePointCommand {
    private String userId;
    private Integer point;
    private String description;
} 