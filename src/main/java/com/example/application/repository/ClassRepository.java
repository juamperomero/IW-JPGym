package com.example.application.repository;

import com.example.application.data.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClassRepository extends JpaRepository<ClassEntity, UUID> {
}
