package com.vfernandes.book;

import com.vfernandes.book.controller.dto.BookDto;
import com.vfernandes.book.utils.dto.BookDtoList;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetAllBooksTest extends BaseIntegrationTest {

    private static final String AUTHOR_NAME = "Author";
    private static final String BOOK_TITLE = "Title";

    @Test
    void getNoBooks() {
        final var response = restTemplate.getForEntity(
                String.format("http://localhost:%d/books", port),
                BookDtoList.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(0, Objects.requireNonNull(response.getBody()).size())
        );
    }

    @Test
    void getBooks() {
        final var firstBook = new BookDto(null, BOOK_TITLE, AUTHOR_NAME);
        final var secondBook = new BookDto(null, BOOK_TITLE + "_2", AUTHOR_NAME + "_2");

        restTemplate.postForEntity(
                String.format("http://localhost:%d/books", port),
                firstBook,
                Void.class);

        restTemplate.postForEntity(
                String.format("http://localhost:%d/books", port),
                secondBook,
                Void.class);

        final var response = restTemplate.getForEntity(
                String.format("http://localhost:%d/books", port),
                BookDtoList.class);

        firstBook.setId(1L);
        secondBook.setId(2L);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(2, Objects.requireNonNull(response.getBody()).size()),
                () -> assertEquals(firstBook, Objects.requireNonNull(response.getBody()).getFirst()),
                () -> assertEquals(secondBook, Objects.requireNonNull(response.getBody()).getLast())
        );
    }

}
