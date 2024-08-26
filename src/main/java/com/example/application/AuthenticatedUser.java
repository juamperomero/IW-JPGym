package com.example.application;

import com.example.application.repository.UserRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.example.application.data.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
    }

    @Transactional
    public UUID getAuthenticatedUserId() {
        Optional<User> user = authenticationContext.getAuthenticatedUser(User.class)
                .flatMap(userDetails -> userRepository.findByUsername(userDetails.getUsername()));

        return user.map(User::getId).orElse(null);
    }

    @Transactional
    public Optional<User> get() {
        return authenticationContext.getAuthenticatedUser(User.class)
                .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()).get());


    }

    public void logout() {
        authenticationContext.logout();
    }

}
