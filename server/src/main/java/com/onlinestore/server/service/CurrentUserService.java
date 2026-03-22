package com.onlinestore.server.service;

import com.onlinestore.server.model.entity.User;
import com.onlinestore.server.repository.UserRepository;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;

@Singleton
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User requireUser(Authentication authentication) {
        if (authentication == null) {
            throw new HttpStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
        Long id = Long.parseLong(authentication.getName());
        return userRepository.findById(id)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
    }
}
