package com.gamewiz10.library.service;

import com.gamewiz10.library.config.TestSecurityConfig;
import com.gamewiz10.library.entity.Author;
import com.gamewiz10.library.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(TestSecurityConfig.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAuthor() {
        Author author = new Author("Author Name", "Biography");
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        Author result = authorService.createAuthor(author);

        assertNotNull(result);
        assertEquals("Author Name", result.getName());
        assertEquals("Biography", result.getBiography());
        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void testGetAllAuthors() {
        List<Author> authors = List.of(
                new Author("Author One", "Biography One"),
                new Author("Author Two", "Biography Two")
        );
        when(authorRepository.findAll()).thenReturn(authors);

        List<Author> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Author One", result.get(0).getName());
        assertEquals("Author Two", result.get(1).getName());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void testGetAuthorById_Found() {
        Author author = new Author("Author Name", "Biography");
        author.setId(1L);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Author result = authorService.getAuthorById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Author Name", result.getName());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAuthorById_NotFound() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authorService.getAuthorById(1L));

        assertEquals("Author not found with id: 1", exception.getMessage());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateAuthor() {
        Author existingAuthor = new Author("Old Name", "Old Biography");
        existingAuthor.setId(1L);

        Author updatedDetails = new Author("Updated Name", "Updated Biography");

        when(authorRepository.findById(1L)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedDetails);

        Author result = authorService.updateAuthor(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals("Updated Biography", result.getBiography());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(existingAuthor);
    }

    @Test
    void testDeleteAuthor() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(1L);

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).existsById(1L);
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void testSearchAuthorsByName() {
        List<Author> authors = List.of(
                new Author("Author One", "Biography One"),
                new Author("Author Two", "Biography Two")
        );

        when(authorRepository.findByNameContainingIgnoreCase("Author")).thenReturn(authors);

        List<Author> result = authorService.searchAuthorsByName("Author");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Author One", result.get(0).getName());
        assertEquals("Author Two", result.get(1).getName());
        verify(authorRepository, times(1)).findByNameContainingIgnoreCase("Author");
    }
}
