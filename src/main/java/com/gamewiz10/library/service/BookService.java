package com.gamewiz10.library.service;

import com.gamewiz10.library.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    Book addBook(Book book);

    List<Book> getAllBooks();

    Optional<Book> getBookById(Long id);

    Book updateBook(Long id, Book bookDetails);

    void deleteBook(Long id);

}
