package com.softuni.stayeasy.service.user;

import com.softuni.stayeasy.model.entity.user.User;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    void register (String username,
                   String password,
                   String email,
                   String firstName,
                   String lastName);

    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void updateUser(User user);
}
