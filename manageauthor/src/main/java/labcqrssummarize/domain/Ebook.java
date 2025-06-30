package labcqrssummarize.domain;

import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ebook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String content;
    private String authorId;

    private Boolean requestPublish = false; // 출간 요청 여부 플래그
    private Boolean isApproved = false;     // 관리자 승인 여부 (기본값 false)

    @PostUpdate
    public void onPostUpdate() {
        if (Boolean.TRUE.equals(this.requestPublish)) {
            RequestPublish event = new RequestPublish(this);
            event.publishAfterCommit();
        }
    }
}
