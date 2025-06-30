package labcqrssummarize.domain;

import lombok.Data;

@Data
public class EstimatePriceAndCategoryCommand {
    private String ebookId;
    private String summary;
    private String content;
}