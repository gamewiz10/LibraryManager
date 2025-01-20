package com.gamewiz10.library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamewiz10.library.config.TestSecurityConfig;
import com.gamewiz10.library.entity.Author;
import com.gamewiz10.library.entity.Book;
import com.gamewiz10.library.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@Import(TestSecurityConfig.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAllBooks() throws Exception {
        Author author = new Author("Author Name", "Author Biography");
        List<Book> books = List.of(
                new Book("Book One", author, "Fiction", 5),
                new Book("Book Two", author, "Non-Fiction", 3)
        );

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book One"))
                .andExpect(jsonPath("$[1].title").value("Book Two"));
    }

    @Test
    public void testCreateBook() throws Exception {
        Author author = new Author("Author Name", "Author Biography");
        Book book = new Book("Book Title", author, "Genre", 10);

        when(bookService.addBook(any(Book.class))).thenReturn(book);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.genre").value("Genre"))
                .andExpect(jsonPath("$.availableCopies").value(10));
    }

    @Test
    public void testGetBookById() throws Exception {
        Author author = new Author("Author Name", "Author Biography");
        Book book = new Book("Book Title", author, "Genre", 10);
        book.setId(1L);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.genre").value("Genre"))
                .andExpect(jsonPath("$.availableCopies").value(10));
    }

    @Test
    public void testUpdateBook() throws Exception {
        Author author = new Author("Author Name", "Author Biography");
        Book updatedBook = new Book("Updated Title", author, "Updated Genre", 8);

        when(bookService.updateBook(Mockito.eq(1L), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.genre").value("Updated Genre"))
                .andExpect(jsonPath("$.availableCopies").value(8));
    }

    @Test
    public void testDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(bookService).deleteBook(1L);
    }
}
