package com.gamewiz10.library.service;

import com.gamewiz10.library.entity.User;

import java.util.Optional;

public interface UserService {

    User createUser(User user);

    Optional<User> getUserById(Long id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
