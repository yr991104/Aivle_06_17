package labcqrssummarize.domain;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;

/**
 * 포인트 변동 내역을 저장하는 엔티티
 * - 지급/차감 내역, 설명, 생성일시 등 기록
 */
@Entity
@Data
public class PointHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String historyId; // 히스토리 ID
    
    private String userId;    // 사용자 ID
    private Integer point;    // 변동 포인트
    private String type;      // 변동 타입 (GIVEN, REDUCED)
    private String description; // 설명
    private Date createdAt;   // 생성 일시

    /**
     * JPA용 기본 생성자
     */
    protected PointHistory() {
        
    }
    
    /**
     * 모든 필드를 초기화하는 생성자
     */
    public PointHistory(String userId, Integer point, String type, String description, Date createdAt) {
        this.userId = userId;
        this.point = point;
        this.type = type;
        this.description = description;
        this.createdAt = createdAt;
    }
}
