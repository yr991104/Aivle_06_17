package labcqrssummarize.domain;

import lombok.Data;

@Data
public class SummarizeContentCommand {
    private String ebookId;
    private String title;
    private String content;
}