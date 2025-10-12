package com.jiocoders.java.jiofamily.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    private static final String TOPIC = "demo_topic";
    private final KafkaTemplate<String, String> kafkaTemplate;

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Message Sent ✅ : " + message);
    }

    public void sendTopicMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
        System.out.println("Message Sent ✅ : " + message);
    }
}
