package labcqrs.domain;

import java.util.*;
import labcqrs.domain.*;
import labcqrs.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class ListOutEbookRequested extends AbstractEvent {

    private String eBookId;
}
