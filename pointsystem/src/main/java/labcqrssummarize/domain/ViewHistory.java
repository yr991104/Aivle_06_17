package labcqrssummarize.domain;

import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class ViewHistory extends AbstractEvent {

    private String userId;
    private String ebookId;
    private String viewedAt;
    private String usedPoint;
    private String subscriptionType;
    private String membershipType;

    public ViewHistory() {
        super();
    }
} 