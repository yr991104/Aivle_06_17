package labcqrssummarize.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GptClient {

    @Value("${openai.api-key}")
    private String apiKey;

    private final WebClient webClient;

    public GptClient() {
        this.webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/chat/completions")
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Content-Type", "application/json")
            .build();
    }

    public String callGpt(String prompt) {
        String body = """
            {
              "model": "gpt-3.5-turbo",
              "messages": [
                { "role": "system", "content": "You are a helpful assistant." },
                { "role": "user", "content": "%s" }
              ]
            }
            """.formatted(prompt);

        try {
            String response = webClient.post()
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block();

            return response;
        } catch (Exception e) {
            return "GPT 호출 실패: " + e.getMessage();
        }
    }
}
