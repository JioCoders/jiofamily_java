package com.jiocoders.java.jiofamily.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.jiocoders.java.jiofamily.kafka.model.UserModel;

@Service
public class MessageProducer {
    private static final String TOPIC = "string_topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public MessageProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Message Sent ✅ : " + message);
    }

    public void sendTopicMessage(String topic, UserModel user) {
        kafkaTemplate.send(topic, user);
        System.out.println("Message Sent ✅ : " + user);
    }
}
