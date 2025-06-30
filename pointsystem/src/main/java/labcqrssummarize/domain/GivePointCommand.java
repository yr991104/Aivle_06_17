package labcqrssummarize.domain;

import lombok.Data;

@Data
public class GivePointCommand {
    private String userId;
    private Integer point;
} 