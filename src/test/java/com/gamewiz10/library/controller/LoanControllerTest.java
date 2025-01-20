package com.gamewiz10.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamewiz10.library.entity.Author;
import com.gamewiz10.library.entity.Book;
import com.gamewiz10.library.entity.Loan;
import com.gamewiz10.library.service.LoanService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoanController.class)
public class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoanService loanService;

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testBorrowBook() throws Exception {
        Author author = new Author("Author Name", "Biography");
        Book book = new Book("Book Title", author, "Genre", 10);

        Loan loan = new Loan();
        loan.setId(1L);
        loan.setLoanDate(LocalDateTime.now());
        loan.setReturned(false);
        loan.setBook(book);

        when(loanService.borrowBook(1L, 1L)).thenReturn(loan);

        mockMvc.perform(post("/api/loans/borrow")
                        .param("userId", "1")
                        .param("bookId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.returned").value(false))
                .andExpect(jsonPath("$.book.title").value("Book Title"));
    }

    @Test
    public void testReturnBook() throws Exception {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setReturned(true);
        loan.setReturnDate(LocalDateTime.now());

        when(loanService.returnBook(1L)).thenReturn(loan);

        mockMvc.perform(put("/api/loans/1/return"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.returned").value(true));
    }

    @Test
    public void testGetAllLoans() throws Exception {
        Loan loan1 = new Loan();
        loan1.setId(1L);
        loan1.setLoanDate(LocalDateTime.now());
        loan1.setReturned(false);

        Loan loan2 = new Loan();
        loan2.setId(2L);
        loan2.setLoanDate(LocalDateTime.now());
        loan2.setReturned(true);

        List<Loan> loans = List.of(loan1, loan2);
        when(loanService.getAllLoans()).thenReturn(loans);

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetLoansByUserId() throws Exception {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setLoanDate(LocalDateTime.now());
        loan.setReturned(false);

        List<Loan> loans = List.of(loan);
        when(loanService.getLoansByUserId(1L)).thenReturn(loans);

        mockMvc.perform(get("/api/loans/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1));
    }

}
