package labcqrssummarize;

import labcqrssummarize.config.kafka.KafkaProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;

/**
 * Pointsystem 메인 애플리케이션 클래스
 * 
 * 주요 기능:
 * - 사용자 포인트 관리 (지급/차감)
 * - 회원가입 시 멤버십 타입별 포인트 자동 지급
 * - 전자책 열람 시 구독 상태별 포인트 차감
 * - Kafka 이벤트 처리
 * - Subscriber 서비스와의 Feign Client 통신
 */
@SpringBootApplication
@EnableBinding(KafkaProcessor.class)  // Kafka 스트림 바인딩 활성화
@EnableFeignClients                   // Feign Client 활성화 (외부 서비스 호출용)
public class PointsystemApplication {

    // 애플리케이션 컨텍스트를 정적 변수로 저장 (도메인 클래스에서 접근용)
    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext =
            SpringApplication.run(PointsystemApplication.class, args);
    }
}
