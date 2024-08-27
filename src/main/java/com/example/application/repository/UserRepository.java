package com.example.application.repository;

import com.example.application.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    List<User> findByActiveTrue();

    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID usuarioId);
}