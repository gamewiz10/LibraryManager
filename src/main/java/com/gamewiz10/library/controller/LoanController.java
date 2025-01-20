package com.gamewiz10.library.controller;

import com.gamewiz10.library.entity.Loan;
import com.gamewiz10.library.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<Loan> borrowBook(@RequestParam Long userId, @RequestParam Long bookId) {
              Loan loan = loanService.borrowBook(userId, bookId);
              return ResponseEntity.ok(loan);
    }

    @PutMapping("/{loanId}/return")
    public ResponseEntity<Loan> returnBook(@PathVariable Long loanId){
        Loan loan = loanService.returnBook(loanId);
        return ResponseEntity.ok(loan);
    }

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Loan>> getLoansByUser(@PathVariable Long userId){
        return ResponseEntity.ok(loanService.getLoansByUserId(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<Loan>> getActiveLoansByUser(@PathVariable Long userId){
        return ResponseEntity.ok(loanService.getActiveLoansByUserId(userId));
    }

    @GetMapping("/{loanId}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long loanId){
        Loan loan = loanService.getLoanByID(loanId);
        return ResponseEntity.ok(loan);
    }

}
