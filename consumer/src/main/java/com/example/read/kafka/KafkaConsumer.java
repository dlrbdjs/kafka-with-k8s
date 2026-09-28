package com.example.read.kafka;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class KafkaConsumer {

    private final MongoClient mongo;

    @Value("${app.mongodb.database}")
    private String databaseName;

    @Value("${app.mongodb.collection}")
    private String collectionName;

    public KafkaConsumer(MongoClient mongo) {
        this.mongo = mongo;
    }

    @KafkaListener(
            topics = "${app.kafka.topic}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumer(String message) throws IOException {
        System.out.println("message: " + message);
        // JSON 파싱
        JSONObject messageObj = new JSONObject(message);

        // Spring이 관리하는 MongoDB 커넥션에 연결
        MongoDatabase database = mongo.getDatabase(databaseName);
        MongoCollection<Document> mongoBooks = database.getCollection(collectionName);

        // 받은 데이터로 삽입할 데이터를 생성
        Document book = new Document();
        book.append("bid", messageObj.getLong("bid"));
        mongoBooks.insertOne(book);
    }
}
