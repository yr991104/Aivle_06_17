package labcqrssummarize.infra;

import javax.naming.NameParser;

import javax.naming.NameParser;
import javax.transaction.Transactional;

import labcqrssummarize.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import labcqrssummarize.domain.*;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler{
    
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}

    @StreamListener(value=KafkaProcessor.INPUT, condition="headers['type']=='RequestPublicationApproved'")
    public void wheneverRequestPublicationApproved_RequestBookCover(@Payload RequestPublicationApproved requestPublicationApproved){

        RequestPublicationApproved event = requestPublicationApproved;
        System.out.println("\n\n##### listener RequestBookCover : " + requestPublicationApproved + "\n\n");


        

        // Sample Logic //

        GenerateBookCoverCommand command = new GenerateBookCoverCommand();
        .generateBookCover(command);
        SummarizeContentCommand command = new SummarizeContentCommand();
        .summarizeContent(command);

        

    }

}

//>>> Clean Arch / Inbound Adaptor

