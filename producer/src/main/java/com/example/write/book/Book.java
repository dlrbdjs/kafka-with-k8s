package com.example.write.book;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String title;

    @Column(length = 50, nullable = false)
    private String author;

    @Column(length = 50, nullable = false)
    private String category;

    private int pages;

    private int price;

    private Date published_date;

    @Column(length = 50, nullable = false)
    private String description;

}
