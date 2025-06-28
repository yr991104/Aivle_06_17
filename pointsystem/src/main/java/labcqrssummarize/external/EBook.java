package labcqrssummarize.external;

import lombok.Data;

@Data
public class EBook {
    private String ebookId;
    private String title;
    private String authorId;
    private String content;
    private String coverImage;
    private String summary;
    private Boolean isPublicationApproved;
    private Integer price;
    private String category;
    private Integer countViews;
    private Object publicationStatus;
} 