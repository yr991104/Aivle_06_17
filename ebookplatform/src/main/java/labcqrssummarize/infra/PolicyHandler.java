package labcqrssummarize.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    EBookPlatformRepository eBookPlatformRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='GenerateBookCover'"
    )
    public void wheneverGenerateBookCover_CheckEbookStatus(
        @Payload GenerateBookCover generateBookCover
    ) {
        GenerateBookCover event = generateBookCover;
        System.out.println(
            "\n\n##### listener CheckEbookStatus : " +
            generateBookCover +
            "\n\n"
        );
        // Comments //
        //AI 표지 생성과 전자책 요약이 모두 된 상태인지 체크

        // Sample Logic //

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='SummarizeContent'"
    )
    public void wheneverSummarizeContent_CheckEbookStatus(
        @Payload SummarizeContent summarizeContent
    ) {
        SummarizeContent event = summarizeContent;
        System.out.println(
            "\n\n##### listener CheckEbookStatus : " + summarizeContent + "\n\n"
        );
        // Comments //
        //AI 표지 생성과 전자책 요약이 모두 된 상태인지 체크

        // Sample Logic //

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestListOutEbook'"
    )
    public void wheneverRequestListOutEbook_ListOutEbook(
        @Payload RequestListOutEbook requestListOutEbook
    ) {
        RequestListOutEbook event = requestListOutEbook;
        System.out.println(
            "\n\n##### listener ListOutEbook : " + requestListOutEbook + "\n\n"
        );

        // Comments //
        //화면에서 전자책 안보이게함

        // Sample Logic //
        EBookPlatform.listOutEbook(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
