package com.vibenavigator.vibe_navigator_api.kafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private static final String TOPIC = "user-interactions-topic";

    @Autowired
    private KafkaTemplate<String,String>kafkaTemplate;

    public void sendInteractionEvent(String message){
        System.out.println("Publishing message to Kafka topic: "+TOPIC);
        this.kafkaTemplate.send(TOPIC,message);
    }
}
