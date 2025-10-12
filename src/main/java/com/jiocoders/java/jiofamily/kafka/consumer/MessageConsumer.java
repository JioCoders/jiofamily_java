package com.jiocoders.java.jiofamily.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.jiocoders.java.jiofamily.kafka.model.UserModel;

@Service
public class MessageConsumer {

    @KafkaListener(topics = "string_topic", groupId = "jio-group")
    public void listen(String message) {
        System.out.println("-------> String Receiver 📩 : " + message);
    }

    @KafkaListener(topics = "object_topic", groupId = "jio-group")
    public void listenObject(UserModel user) {
        System.out.println("-------> Object Receiver 📩 : " + user);
    }
}
