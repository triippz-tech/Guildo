package com.triippztech.guildo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BotKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(BotKafkaConsumer.class);
    private static final String TOPIC = "topic_bot";

    @KafkaListener(topics = "topic_bot", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
}
