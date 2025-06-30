package labcqrssummarize.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.transaction.Transactional;
import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.*;
import labcqrssummarize.external.GptClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    EBookRepository eBookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestPublishApproved'"
    )
    public void wheneverRequestPublishApproved_GenerateContentWithAi(
        @Payload RequestPublishApproved requestPublishApproved
    ) {
        System.out.println("\n\n##### listener GenerateContentWithAi : " + requestPublishApproved + "\n\n");

        try {
            // GPT 호출 프롬프트 구성
            String prompt = "책 제목: " + requestPublishApproved.getTitle() + "\n"
                + "책 내용: " + requestPublishApproved.getContent() + "\n"
                + "\n이 책의 내용을 요약하고, 어울리는 표지 설명, 가격, 카테고리를 생성해줘. 다음 형식으로:\n"
                + "요약: ...\n표지 설명: ...\n가격: ...\n카테고리: ...";

            // GPT 호출
            GptClient gptClient = ApplicationContextProvider.applicationContext.getBean(GptClient.class);
            String result = gptClient.callGpt(prompt);
            System.out.println("📌 GPT 응답 결과:\n" + result);

            // 결과 파싱
            String[] lines = result.split("\n");
            String summary = lines[0].replace("요약:", "").trim();
            String coverDesc = lines[1].replace("표지 설명:", "").trim();
            int price = Integer.parseInt(lines[2].replace("가격:", "").trim());
            String category = lines[3].replace("카테고리:", "").trim();

            // EBook 업데이트 및 저장
            EBook ebook = eBookRepository.findById(requestPublishApproved.getEbookId()).orElse(null);
            if (ebook != null) {
                ebook.setSummary(summary);
                ebook.setCoverImage(coverDesc);
                ebook.setPrice(price);
                ebook.setCategory(category);
                eBookRepository.save(ebook);
                System.out.println("✅ EBook 업데이트 완료: " + ebook);
            } else {
                System.out.println("❌ 해당 ID의 EBook을 찾을 수 없습니다: " + requestPublishApproved.getEbookId());
            }

        } catch (Exception e) {
            System.out.println("❌ GPT 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
//>>> Clean Arch / Inbound Adaptor
