package org.example.baitapbig.repository;

import org.example.baitapbig.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByCategory(String category);
}
