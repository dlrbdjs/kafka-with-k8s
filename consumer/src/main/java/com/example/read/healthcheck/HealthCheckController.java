package com.example.read.healthcheck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final MongoTemplate mongoTemplate;
    private final KafkaAdmin kafkaAdmin;

    @GetMapping("/health/ready")
    public ResponseEntity<String> ready() {

        // 1. MongoDB 확인
        try {
            Document result = mongoTemplate
                    .getDb()
                    .runCommand(new Document("ping", 1));

            Object ok = result.get("ok");

            if (!(ok instanceof Number) ||
                    ((Number) ok).doubleValue() != 1.0) {

                log.warn("[READINESS] MongoDB is not ready");

                return ResponseEntity.status(503)
                        .body("mongodb not ready");
            }

        } catch (Exception e) {
            log.warn("[READINESS] MongoDB connection failed: {}", e.getMessage());

            return ResponseEntity.status(503)
                    .body("mongodb not ready");
        }


        // 2. Kafka 확인
        try (AdminClient adminClient =
                     AdminClient.create(kafkaAdmin.getConfigurationProperties())) {

            adminClient.describeCluster()
                    .clusterId()
                    .get(1, TimeUnit.SECONDS);

        } catch (Exception e) {
            log.warn("[READINESS] Kafka connection failed: {}", e.getMessage());

            return ResponseEntity.status(503)
                    .body("kafka not ready");
        }


        // 3. 둘 다 정상
        return ResponseEntity.ok("ready");
    }


    @GetMapping("/health/app")
    public String healthCheck() {
        return "argo cd success";
    }
}