package com.gamewiz10.library.service;

import com.gamewiz10.library.entity.Author;
import com.gamewiz10.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService{

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
    }

    @Override
    public Author updateAuthor(Long id, Author authorDetails) {
        return authorRepository.findById(id).map(existingAuthor -> {
            existingAuthor.setName(authorDetails.getName());
            existingAuthor.setBiography(authorDetails.getBiography());

            if (authorDetails.getBooks() != null) {
                existingAuthor.setBooks(authorDetails.getBooks());
            }

            return authorRepository.save(existingAuthor);
        }).orElseThrow(() -> new IllegalArgumentException("Author with ID " + id + " not found"));
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new IllegalArgumentException("Author with ID " + id + " not found");
        }
        authorRepository.deleteById(id);
    }

    @Override
    public List<Author> searchAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }
}
