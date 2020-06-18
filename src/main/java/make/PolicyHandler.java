package make;

import make.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{

    @Autowired
    MakeRepository makeRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverOrdered_Make(@Payload Ordered ordered){
        if(ordered.isMe()){
            Make make = new Make();
            make.setOrderId(ordered.getId());
            make.setStatus("WAITING");
            makeRepository.save(make);
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCancelled_Make(@Payload Cancelled cancelled){
        if(cancelled.isMe()){
            Make make = makeRepository.findByOrderId(cancelled.getId());
            make.setOrderId(cancelled.getId());
            make.setStatus("CANCELLED");
            makeRepository.save(make);
        }
    }

}
