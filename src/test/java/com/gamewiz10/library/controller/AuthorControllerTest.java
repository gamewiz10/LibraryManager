package com.gamewiz10.library.controller;

import com.gamewiz10.library.config.TestSecurityConfig;
import com.gamewiz10.library.entity.Author;
import com.gamewiz10.library.service.AuthorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


@WebMvcTest(AuthorController.class)
@Import(TestSecurityConfig.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Test
    public void testGetAllAuthors() throws Exception {
        List<Author> authors = List.of(
                new Author("Author One", "Biography One"),
                new Author("Author Two", "Biography Two")
        );

        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Author One"))
                .andExpect(jsonPath("$[1].name").value("Author Two"));
    }

    @Test
    public void testCreateAuthor() throws Exception {
        Author author = new Author("Author Name", "Author Biography");

        when(authorService.createAuthor(any(Author.class))).thenReturn(author);

        mockMvc.perform(post("/api/authors").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Author Name\", \"biography\": \"Author Biography\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Author Name"))
                .andExpect(jsonPath("$.biography").value("Author Biography"));
    }

    @Test
    public void testGetAuthorById() throws Exception {
        Author author = new Author("Author Name", "Author Biography");
        author.setId(1L);

        when(authorService.getAuthorById(1L)).thenReturn(author);

        mockMvc.perform(get("/api/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Author Name"))
                .andExpect(jsonPath("$.biography").value("Author Biography"));
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        Author updatedAuthor = new Author("Updated Name", "Updated Biography");

        when(authorService.updateAuthor(Mockito.eq(1L), any(Author.class))).thenReturn(updatedAuthor);

        mockMvc.perform(put("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Name\", \"biography\": \"Updated Biography\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.biography").value("Updated Biography"));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/authors/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(authorService).deleteAuthor(1L);
    }

    @Test
    public void testSearchAuthorsByName() throws Exception {
        List<Author> authors = List.of(
                new Author("Author One", "Biography One"),
                new Author("Author Two", "Biography Two")
        );

        when(authorService.searchAuthorsByName("Author")).thenReturn(authors);

        mockMvc.perform(get("/api/authors/search?name=Author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Author One"))
                .andExpect(jsonPath("$[1].name").value("Author Two"));
    }




}

