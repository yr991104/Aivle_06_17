package labcqrssummarize.external;

import java.util.Date;
import lombok.Data;

@Data
public class UserPoint {

    private String userId;
    private Integer point;
    private Object pointHistory;
}
