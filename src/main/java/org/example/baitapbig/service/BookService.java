package org.example.baitapbig.service;

import org.example.baitapbig.model.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService {
    public List<Book> getAllBooks();
    public Book getBookById(Integer id);
    public Book saveBook(Book book);
    public Boolean deleteBook(Integer id);
    public Book updateBook(Book book, MultipartFile image);

}
