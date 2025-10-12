package com.jiocoders.java.jiofamily.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @KafkaListener(topics = "demo_topic", groupId = "demo-group")
    public void listen(String message) {
        System.out.println("-------> Receiver 📩 : " + message);
    }
}
