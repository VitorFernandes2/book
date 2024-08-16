package com.vfernandes.book.utils;

import org.testcontainers.containers.PostgreSQLContainer;

public final class BookPostgresqlContainer extends PostgreSQLContainer<BookPostgresqlContainer> {

    private static final String IMAGE_VERSION = "postgres:15.6";
    private static BookPostgresqlContainer container;

    private BookPostgresqlContainer() {
        super(IMAGE_VERSION);
    }

    public static BookPostgresqlContainer getInstance() {
        if (container == null) {
            container = new BookPostgresqlContainer();
        }
        return container;
    }

    @Override
    public void start() {
        super.start();
    }

}
