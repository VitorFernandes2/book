package com.vfernandes.book;

import com.vfernandes.book.controller.dto.BookDto;
import com.vfernandes.book.utils.dto.BookDtoList;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InternalServerErrorsTest extends BaseIntegrationTest {

    private static final String AUTHOR_NAME = "Author";
    private static final String BOOK_TITLE = "Title";

    @Test
    void errorGettingBook() {
        postgreSQLContainer.stop();

        try {

            restTemplate.getForEntity(
                    String.format("http://localhost:%d/books/1", port),
                    BookDtoList.class);

        } catch (final HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }

    }

    @Test
    void errorGettingBooks() {
        postgreSQLContainer.stop();

        try {

            restTemplate.getForEntity(
                    String.format("http://localhost:%d/books", port),
                    BookDtoList.class);

        } catch (final HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }

    }

    @Test
    void errorCreatingBookDatabaseDown() {
        postgreSQLContainer.stop();

        final var bookDto = new BookDto(null, BOOK_TITLE, AUTHOR_NAME);

        try {

            restTemplate.postForEntity(
                    String.format("http://localhost:%d/books", port),
                    bookDto,
                    Void.class);

        } catch (final HttpServerErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }

    }

}
