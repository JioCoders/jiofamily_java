package com.jiocoders.java.jiofamily.entity;

import org.springframework.stereotype.Component;

@Component
public class Message {
    public static String getHealth() {
        return "Bean Health is up and running!";
    }
}
