package com.gamewiz10.library.service;

import com.gamewiz10.library.entity.Book;
import com.gamewiz10.library.entity.Loan;
import com.gamewiz10.library.repository.BookRepository;
import com.gamewiz10.library.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService{

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;

    @Autowired
    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository){
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Loan borrowBook(Long userId, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));
        if (book.getAvailableCopies() <= 0){
            throw new IllegalArgumentException("No copies available for loan");
        }
        book.setAvailableCopies(book.getAvailableCopies() -1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setId(userId);
        loan.setBook(book);
        loan.setLoanDate(LocalDateTime.now());
        loan.setReturned(false);
        return loanRepository.save(loan);
    }

    @Override
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        if (loan.isReturned()) {
            throw new IllegalArgumentException("Loan is already returned");
        }
        Book book = new Book();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        loan.setReturned(true);
        loan.setReturnDate(LocalDateTime.now());
        return loanRepository.save(loan);
    }

    @Override
    public List<Loan> getLoansByUserId(Long userId) {
        return loanRepository.findByUserId(userId);
    }

    @Override
    public List<Loan> getActiveLoansByUserId(Long userId) {
        return loanRepository.findByUserIdAndReturnFalse(userId);
    }

    @Override
    public List<Loan> getActiveLoansByBookId(Long bookId) {
        return loanRepository.findByBookId(bookId).stream()
                .filter(loan -> !loan.isReturned())
                .toList();
    }

    @Override
    public Loan getLoanByID(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }
}
