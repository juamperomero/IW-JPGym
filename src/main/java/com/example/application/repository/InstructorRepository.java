package com.example.application.repository;

import com.example.application.data.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}
