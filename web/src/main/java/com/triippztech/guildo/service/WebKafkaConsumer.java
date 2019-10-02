package com.triippztech.guildo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class WebKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(WebKafkaConsumer.class);
    private static final String TOPIC = "topic_web";

    @KafkaListener(topics = "topic_web", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
}
