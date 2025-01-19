package com.gamewiz10.library.service;

import com.gamewiz10.library.entity.Book;
import com.gamewiz10.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBook(Book book) {
        if(book.getTitle() == null || book.getTitle().isEmpty()){
            throw new IllegalArgumentException("Book title cannot be empty");
        }
        if(book.getAvailableCopies() < 0){
            throw new IllegalArgumentException("Available copies cannot be negative");
        }
        return bookRepository.save(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Book updateBook(Long id, Book bookDetails) {
        return bookRepository.findById(id).map(existingBook -> {
            existingBook.setTitle(bookDetails.getTitle());
            existingBook.setGenre(bookDetails.getGenre());
            existingBook.setAvailableCopies(bookDetails.getAvailableCopies());
            existingBook.setAuthor(bookDetails.getAuthor());
            return bookRepository.save(existingBook);
        }).orElseThrow(() -> new IllegalArgumentException("Book with ID" + id + "not found"));
    }

    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)){
            throw new IllegalArgumentException("Book with ID " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

}
