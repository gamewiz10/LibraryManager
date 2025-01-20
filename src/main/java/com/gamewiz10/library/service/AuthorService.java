package com.gamewiz10.library.service;

import com.gamewiz10.library.entity.Author;

import java.util.List;

public interface AuthorService {

    Author createAuthor(Author author);

    List<Author> getAllAuthors();

    Author getAuthorById(Long id);

    Author updateAuthor(Long id, Author authorDetails);

    void deleteAuthor(Long id);

    List<Author> searchAuthorsByName(String name);
}
