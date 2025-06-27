package labcqrssummarize.external;

import java.util.Date;
import lombok.Data;

@Data
public class EBookPlatform {

    private Integer pid;
    private String ebooks;
    private String aiGeneratedCover;
    private Date registeredAt;
}
