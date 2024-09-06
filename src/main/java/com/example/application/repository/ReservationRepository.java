package com.example.application.repository;

import com.example.application.data.ClassEntity;
import com.example.application.data.Reservation;
import com.example.application.data.ReservationStatus;
import com.example.application.data.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"classEntity", "user"})
    List<Reservation> findByUser(User user);
    List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findByClassEntityAndStatus(ClassEntity classEntity, ReservationStatus status);
    List<Reservation> findByClassEntity_ScheduleBetweenAndStatus(LocalDateTime start, LocalDateTime end, ReservationStatus status);
}
