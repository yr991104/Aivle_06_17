package labcqrssummarize.external;

import java.util.Date;
import lombok.Data;

@Data
public class EBookPlatform {

    private Long id;
    private String ebookId;
    private String authorId;
    private String contentId;
    private String aiGeneratedCover;
    private String ebookStatus;
    private Date registeredAt;
}
