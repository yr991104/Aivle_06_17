package labcqrssummarize;

import labcqrssummarize.config.kafka.KafkaProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableBinding(KafkaProcessor.class)
@EnableFeignClients
public class AisystemApplication {

    public static ApplicationContext applicationContext;

    public static void main(String[] args) {
        applicationContext =
            SpringApplication.run(AisystemApplication.class, args);
    }
}

    // ✅ 테스트용 GPT 호출 실행
    @Bean
    public CommandLineRunner testGpt(GptClient gptClient) {
        return args -> {
            String prompt = "간단한 테스트 질문입니다. 오늘 날씨 어때요?";
            String result = gptClient.callGpt(prompt);
            System.out.println("✅ GPT 응답 결과:\n" + result);
        };
    }
}