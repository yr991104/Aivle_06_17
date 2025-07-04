package labcqrssummarize.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class OpenAIService {

    private final WebClient webClient;

    @Value("${openai.api.key}")
    private String apiKey;

    public OpenAIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
    }

    public String summarizeText(String content) {
        List<Map<String, Object>> messages = Collections.singletonList(new HashMap<String, Object>() {{
            put("role", "user");
            put("content", "다음 내용을 간결하고 명확하게 요약해줘:\n" + content);
        }});
        return extractMessageContent(callChatCompletion(messages, 500, 0.7));
    }

    public String generateCoverImage(String title) {
        String prompt = String.format(
            "Create a soft, elegant, and emotionally resonant book cover illustration for a book titled \"%s\". " +
            "Use gentle color palettes like pastel tones and abstract shapes that suggest imagination, calmness, or emotional depth. " +
            "Do not include any text, letters, or characters in the image. Focus on creating a dreamy, minimalistic, and artistic atmosphere.",
            title
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        requestBody.put("n", 1);
        requestBody.put("size", "1024x1024");

        Map<String, Object> response = webClient.post()
            .uri("/images/generations")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .block();

        if (response != null && response.get("data") instanceof List) {
            List<?> dataList = (List<?>) response.get("data");
            if (!dataList.isEmpty()) {
                Object first = dataList.get(0);
                if (first instanceof Map) {
                    Map<?, ?> map = (Map<?, ?>) first;
                    Object urlObj = map.get("url");
                    if (urlObj instanceof String) {
                        return (String) urlObj;
                    }
                }
            }
        }
        return "";
    }

    public String estimateCategory(String summary) {
        List<Map<String, Object>> messages = Collections.singletonList(new HashMap<String, Object>() {{
            put("role", "user");
            put("content", "다음 줄거리를 기반으로 적절한 전자책 카테고리를 한 단어로 추천해줘:\n" + summary);
        }});
        String category = extractMessageContent(callChatCompletion(messages, 20, 0.5));
        return (category != null) ? category.trim() : "Unknown";
    }

    public Integer estimatePrice(String summary) {
        List<Map<String, Object>> messages = Collections.singletonList(new HashMap<String, Object>() {{
            put("role", "user");
            put("content", "다음 전자책 줄거리 기반으로 적절한 구독료를 '숫자만' 한국 원화로 정수 형태로 추천해줘. 예: 1500\n" + summary);
        }});
        String priceStr = extractMessageContent(callChatCompletion(messages, 10, 0.3));
        if (priceStr != null) {
            priceStr = priceStr.replaceAll("[^0-9]", "");
            if (!priceStr.isEmpty()) {
                try {
                    return Integer.parseInt(priceStr);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ 가격 파싱 실패: " + e.getMessage());
                }
            }
        }
        return 1000;
    }

    private Map<String, Object> callChatCompletion(List<Map<String, Object>> messages, int maxTokens, double temperature) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o-mini");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", maxTokens);
        requestBody.put("temperature", temperature);

        return webClient.post()
            .uri("/chat/completions")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(requestBody)
            .retrieve()
            .bodyToMono(Map.class)
            .block();
    }

    private String extractMessageContent(Map<String, Object> response) {
        if (response != null && response.get("choices") instanceof List) {
            List<?> choices = (List<?>) response.get("choices");
            if (!choices.isEmpty()) {
                Object first = choices.get(0);
                if (first instanceof Map) {
                    Map<?, ?> firstMap = (Map<?, ?>) first;
                    Object messageObj = firstMap.get("message");
                    if (messageObj instanceof Map) {
                        Map<?, ?> messageMap = (Map<?, ?>) messageObj;
                        Object contentObj = messageMap.get("content");
                        if (contentObj instanceof String) {
                            return (String) contentObj;
                        }
                    }
                }
            }
        }
        return null;
    }
}