package com.example.application.service;

import com.example.application.data.ClassEntity;
import com.example.application.data.Reservation;
import com.example.application.data.ReservationStatus;
import com.example.application.data.User;
import com.example.application.repository.ReservationRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> findAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> findReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    @Transactional
    public Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public void updateReservationStatus(Long id, ReservationStatus status) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservation.get().setStatus(status);
            reservationRepository.save(reservation.get());
        }
    }

    @Transactional(readOnly = true)
    public List<Reservation> findReservationsByUser(User user) {
        return reservationRepository.findByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Reservation> findReservationsByStatus(ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByStatus(status);
        reservations.forEach(reservation -> {
            Hibernate.initialize(reservation.getClassEntity()); // Inicializa la entidad ClassEntity
            Hibernate.initialize(reservation.getUser()); // Inicializa la entidad User
        });
        return reservations;
    }

    @Transactional(readOnly = true)
    public List<Reservation> findReservationsByClassEntityAndStatus(ClassEntity classEntity, ReservationStatus status) {
        List<Reservation> reservations = reservationRepository.findByClassEntityAndStatus(classEntity, status);
        reservations.forEach(reservation -> {
            Hibernate.initialize(reservation.getClassEntity()); // Inicializa la entidad ClassEntity
            Hibernate.initialize(reservation.getUser()); // Inicializa la entidad User
        });
        return reservations;
    }

    public List<Reservation> findReservationsBetween(LocalDateTime start, LocalDateTime end, ReservationStatus status) {
        return reservationRepository.findByClassEntity_ScheduleBetweenAndStatus(start, end, status);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}