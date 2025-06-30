package labcqrssummarize.domain;

import lombok.Data;

/**
 * 포인트 지급 명령을 위한 커맨드 객체
 * - userId: 대상 사용자 ID
 * - point: 지급할 포인트
 * - description: 지급 사유
 */
@Data
public class GivePointCommand {
    private String userId;      // 대상 사용자 ID
    private Integer point;      // 지급할 포인트
    private String description; // 지급 사유
} 