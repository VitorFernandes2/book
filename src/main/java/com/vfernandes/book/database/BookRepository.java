package com.vfernandes.book.database;

import com.vfernandes.book.database.dao.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
