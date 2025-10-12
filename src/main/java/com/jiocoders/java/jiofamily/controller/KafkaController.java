package com.jiocoders.java.jiofamily.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jiocoders.java.jiofamily.kafka.producer.MessageProducer;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final MessageProducer messageProducer;

    public KafkaController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    // curl "http://localhost:8080/api/kafka/send?msg=helloKafkaGet"
    @GetMapping("/send")
    public String sendMessage(@RequestParam String msg) {
        messageProducer.sendMessage(msg);
        return "Message sent to Kafka✅ : " + msg;
    }
    
    // curl -X POST http://localhost:8080/api/kafka/send/helloKafka
    @PostMapping("send/{msg}")
    public String sendPostMessage(@PathVariable String msg) {
        messageProducer.sendTopicMessage("demo_topic", msg);
        return "✅ Message sent to Kafka: " + msg;
    }
}