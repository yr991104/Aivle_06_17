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
    AuthorRepository authorRepository;

    @Autowired
    EBookRepository eBookRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RegisteredAuthor'"
    )
    public void wheneverRegisteredAuthor_HandleAuthorRegistrationRequest(
        @Payload RegisteredAuthor registeredAuthor
    ) {
        RegisteredAuthor event = registeredAuthor;
        System.out.println(
            "\n\n##### listener HandleAuthorRegistrationRequest : " +
            registeredAuthor +
            "\n\n"
        );
        // Sample Logic //

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='WrittenContent'"
    )
    public void wheneverWrittenContent_HandleContentRegistrationRequest(
        @Payload WrittenContent writtenContent
    ) {
        WrittenContent event = writtenContent;
        System.out.println(
            "\n\n##### listener HandleContentRegistrationRequest : " +
            writtenContent +
            "\n\n"
        );
        // Comments //
        //해당 책은 출판된것은 아니고 본인의 서재에서만 확인할 수 있는 본인의 책, 해당 책은 보안 문제만 검열함.(ex. SQL Injection, 서버에 위협을 줄 수 있는 코드 문장 삽입 여부)
        //
        // 굳이 관리자가 안해도 자동으로 요청 처리하는게 편할거 같음

        // Sample Logic //

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestPublish'"
    )
    public void wheneverRequestPublish_HandlePublishRequest(
        @Payload RequestPublish requestPublish
    ) {
        RequestPublish event = requestPublish;
        System.out.println(
            "\n\n##### listener HandlePublishRequest : " +
            requestPublish +
            "\n\n"
        );
        // Sample Logic //

    }
}
//>>> Clean Arch / Inbound Adaptor
