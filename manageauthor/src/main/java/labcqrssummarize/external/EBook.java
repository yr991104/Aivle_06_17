package labcqrssummarize.external;

import java.util.Date;
import lombok.Data;

@Data
public class EBook {

    private String ebookId;
    private String title;
    private String authorId;
    private String contentId;
    private String coverImage;
    private String summary;
    private Boolean isPublicationApproved;
    private String publicationStatus;
}
