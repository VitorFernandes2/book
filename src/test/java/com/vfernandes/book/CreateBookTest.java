package com.vfernandes.book;

import com.vfernandes.book.controller.dto.BookDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CreateBookTest extends BaseIntegrationTest {

    private static final String AUTHOR_NAME = "Author";
    private static final String BOOK_TITLE = "Title";

    @Test
    void createBookWithSuccess() {
        final var bookDto = new BookDto(null, BOOK_TITLE, AUTHOR_NAME);

        final var response = restTemplate.postForEntity(
                String.format("http://localhost:%d/books", port),
                bookDto,
                Void.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void errorCreatingBookNoTitle() {
        final var bookDto = new BookDto(null, null, AUTHOR_NAME);

        try {

            restTemplate.postForEntity(
                    String.format("http://localhost:%d/books", port),
                    bookDto,
                    Void.class);

        } catch (final HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }

    }

    @Test
    void errorCreatingBookNoAuthor() {
        final var bookDto = new BookDto(null, BOOK_TITLE, null);

        try {

            restTemplate.postForEntity(
                    String.format("http://localhost:%d/books", port),
                    bookDto,
                    Void.class);

        } catch (final HttpClientErrorException e) {
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }

    }

}
