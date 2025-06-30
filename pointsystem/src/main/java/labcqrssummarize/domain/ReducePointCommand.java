package labcqrssummarize.domain;

import lombok.Data;

/**
 * 포인트 차감 명령을 위한 커맨드 객체
 * - userId: 대상 사용자 ID
 * - point: 차감할 포인트
 * - description: 차감 사유
 */
@Data
public class ReducePointCommand {
    private String userId;      // 대상 사용자 ID
    private Integer point;      // 차감할 포인트
    private String description; // 차감 사유
} 