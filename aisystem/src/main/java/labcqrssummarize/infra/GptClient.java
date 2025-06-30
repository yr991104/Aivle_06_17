package labcqrssummarize.external;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GptClient {

    @Value("${openai.api-key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
        .baseUrl("https://api.openai.com/v1/chat/completions")
        .defaultHeader("Authorization", "Bearer " + apiKey)
        .defaultHeader("Content-Type", "application/json")
        .build();

    public String callGpt(String prompt) {
        String requestBody = "{\n" +
            "  \"model\": \"gpt-4\",\n" +
            "  \"messages\": [\n" +
            "    {\"role\": \"system\", \"content\": \"You are a helpful assistant for ebook publishing.\"},\n" +
            "    {\"role\": \"user\", \"content\": \"" + prompt.replace("\"", "\\\"") + "\"}\n" +
            "  ],\n" +
            "  \"temperature\": 0.7\n" +
            "}";

        try {
            String response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            // GPT 응답에서 메시지 내용 추출 (단순 추출용)
            int start = response.indexOf("\"content\":\"") + 10;
            int end = response.indexOf("\"", start);
            return response.substring(start, end).replace("\\n", "\n").replace("\\\"", "\"");

        } catch (Exception e) {
            System.out.println("❌ GPT 호출 실패: " + e.getMessage());
            return "GPT 호출 실패";
        }
    }
}
