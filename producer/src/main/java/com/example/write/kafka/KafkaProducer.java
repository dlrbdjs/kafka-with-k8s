package com.example.write.kafka;

import com.example.write.book.BookDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final KafkaAdmin kafkaAdmin;

    @Value("${app.kafka.topic}")
    private String topic;

    public void sendMessage(BookDTO bookDTO) {
        String message = "{\"bid\":" + "\"" + bookDTO.getBid() + "\"}";
        // 메시지 전송
        kafkaTemplate.send(topic, message);
    }
}
