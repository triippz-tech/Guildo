package com.triippztech.guildo.web.rest;

import com.triippztech.guildo.service.BotKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/bot-kafka")
public class BotKafkaResource {

    private final Logger log = LoggerFactory.getLogger(BotKafkaResource.class);

    private BotKafkaProducer kafkaProducer;

    public BotKafkaResource(BotKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
