package com.example.application.repository;

import com.example.application.data.ClassEntity;
import com.example.application.data.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Set<ClassEntity> findClassesByAttendees(User user);
}
