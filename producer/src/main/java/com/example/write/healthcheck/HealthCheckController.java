package com.example.write.healthcheck;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckController {

    private final DataSource dataSource;
    private final KafkaAdmin kafkaAdmin;

    @GetMapping("/health/ready")
    public ResponseEntity<String> ready() {

        // 1. MySQL 확인
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT 1")) {

            statement.setQueryTimeout(1);

            try (var result = statement.executeQuery()) {
                if (!result.next() || result.getInt(1) != 1) {
                    log.warn("[READINESS] MySQL is not ready");
                    return ResponseEntity.status(503)
                            .body("mysql not ready");
                }
            }

        } catch (java.sql.SQLException e) {
            log.warn("[READINESS] MySQL connection failed: {}", e.getMessage());

            return ResponseEntity.status(503)
                    .body("mysql not ready");
        }

        // 2. Kafka 확인
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {

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

    @GetMapping("/health/error")
    public String healthCheck() {
        return "success";
    }
}