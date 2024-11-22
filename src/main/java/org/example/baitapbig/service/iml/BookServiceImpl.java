package org.example.baitapbig.service.iml;

import org.example.baitapbig.model.Book;
import org.example.baitapbig.repository.BookRepository;
import org.example.baitapbig.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository bookRepository;
    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    @Override
    public Book getBookById(Integer id) {
        Book book = bookRepository.findById(id).orElse(null);
        return book;
    }
    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }
    @Override
    public Boolean deleteBook(Integer id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (!ObjectUtils.isEmpty(book)) {
            bookRepository.delete(book);
            return true;
        }
        return false;
    }
    @Override
    public Book updateBook(Book book, MultipartFile image) {

        Book books = getBookById(book.getId());

        String imageName = image.isEmpty() ? books.getImage() : image.getOriginalFilename();

        books.setTitle(book.getTitle());
        books.setAuthor(book.getAuthor());
        books.setCategory(book.getCategory());
        books.setPrice(book.getPrice());
        books.setImage(imageName);

        Book updateBook = bookRepository.save(books);

        if (!ObjectUtils.isEmpty(updateBook)) {

            if (!image.isEmpty()) {

                try {
                    File saveFile = new ClassPathResource("static/img").getFile();

                    Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "product_img" + File.separator
                            + image.getOriginalFilename());
                    Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return book;
        }
        return null;
    }
}
