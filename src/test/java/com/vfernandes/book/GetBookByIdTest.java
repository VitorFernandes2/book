package com.vfernandes.book;

import com.vfernandes.book.controller.dto.BookDto;
import com.vfernandes.book.utils.dto.BookDtoList;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetBookByIdTest extends BaseIntegrationTest {

    private static final String AUTHOR_NAME = "Author";
    private static final String BOOK_TITLE = "Title";

    @Test
    void bookNotFound() {
        try {

            restTemplate.getForEntity(
                    String.format("http://localhost:%d/books/1", port),
                    BookDtoList.class);

        } catch (final HttpClientErrorException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
        } finally {
            postgreSQLContainer.start();
        }
    }

    @Test
    void getBookById() {
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
                String.format("http://localhost:%d/books/2", port),
                BookDto.class);

        secondBook.setId(2L);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(secondBook, response.getBody())
        );
    }

}
