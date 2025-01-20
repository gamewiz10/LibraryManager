package com.gamewiz10.library.service;

import com.gamewiz10.library.entity.Loan;

import java.util.List;

public interface LoanService {

    Loan borrowBook(Long userId, Long bookId);

    Loan returnBook(Long loanId);

    List<Loan> getLoansByUserId(Long userId);

    List<Loan> getActiveLoansByUserId(Long userId);

    List<Loan> getActiveLoansByBookId(Long bookId);

    Loan getLoanByID(Long loanId);

    List<Loan> getAllLoans();
}
