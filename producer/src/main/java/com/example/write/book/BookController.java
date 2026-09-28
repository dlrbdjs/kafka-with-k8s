package com.example.write.book;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/")
    public String index() {
        return "I am consumer";
    }

    @PostMapping("/cqrs/book")
    public String saveBook(
            @RequestBody BookDTO bookDTO
    ) {
        bookService.saveBook(bookDTO);
        return "책 생성 완료";
    }


}
