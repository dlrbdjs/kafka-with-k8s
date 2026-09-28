package com.example.read.book;

import com.mongodb.client.*;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final MongoClient mongoClient;

    @Value("${app.mongodb.database}")
    private String databaseName;

    @Value("${app.mongodb.collection}")
    private String collectionName;

    @GetMapping("/")
    public String index() {
        return "homepage";
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "success";
    }

    @GetMapping("/cqrs/book")
    public ResponseEntity<?> getBooks() {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> mongoBooks =
                database.getCollection(collectionName);
        List<Document> list = new ArrayList<>();
        try {
            try (MongoCursor<Document> cur = mongoBooks.find().iterator()) {
                while (cur.hasNext()) {
                    Document doc = cur.next();
                    list.add(doc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

}
