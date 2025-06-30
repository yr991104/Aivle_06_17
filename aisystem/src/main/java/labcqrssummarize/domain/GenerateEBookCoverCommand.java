package labcqrssummarize.domain;

import lombok.Data;

@Data
public class GenerateEBookCoverCommand {
    private String ebookId;
    private String title;
    private String authorId;
    private String content;
}