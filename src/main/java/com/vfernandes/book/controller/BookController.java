package com.vfernandes.book.controller;

import com.vfernandes.book.controller.dto.BookDto;
import com.vfernandes.book.database.BookRepository;
import com.vfernandes.book.database.dao.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
final class BookController {

    private final BookRepository bookRepository;

    @PostMapping
    ResponseEntity<Void> createBook(@RequestBody final BookDto bookDto) {
        try {

            if (!StringUtils.hasText(bookDto.getTitle()) || !StringUtils.hasText(bookDto.getAuthor())) {
                log.warn("Invalid book data: {}", bookDto);
                return ResponseEntity.badRequest().build();
            }

            final var book = new Book(null, bookDto.getTitle(), bookDto.getAuthor());

            bookRepository.save(book);
            log.info("Book created: {}", book);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException exception) {
            log.error("Error creating book", exception);
            return ResponseEntity.internalServerError().build();
        }

    }

    @GetMapping
    ResponseEntity<List<BookDto>> getAllBooks() {
        try {
            final List<Book> books = bookRepository.findAll();

            if (books.isEmpty()) {
                log.warn("No books found");
                return ResponseEntity.ok(List.of());
            }

            return ResponseEntity.ok(books.stream()
                    .map(book -> new BookDto(book.getId(), book.getTitle(), book.getAuthor()))
                    .toList());
        } catch (RuntimeException exception) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{_id}")
    ResponseEntity<BookDto> getBookById(@PathVariable("_id") final Long id) {
        try {
            final Optional<Book> book = bookRepository.findById(id);

            return book.map(value -> ResponseEntity.ok(new BookDto(value.getId(), value.getTitle(), value.getAuthor())))
                    .orElseGet(() -> {
                        log.warn("Book not found with id: {}", id);
                        return ResponseEntity.notFound().build();
                    });

        } catch (RuntimeException exception) {
            log.error("Error getting book by id", exception);
            return ResponseEntity.internalServerError().build();
        }
    }

}
