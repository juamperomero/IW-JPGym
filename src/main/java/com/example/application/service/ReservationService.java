package com.example.application.service;

import com.example.application.data.Reservation;
import com.example.application.data.ReservationStatus;
import com.example.application.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Reservation> findReservationById(UUID id) {
        return reservationRepository.findById(id);
    }

    public Reservation saveReservation(Reservation reservation) {
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setStatus(ReservationStatus.PENDIENTE);
        return reservationRepository.save(reservation);
    }

    public void updateReservationStatus(UUID id, ReservationStatus status) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            reservation.get().setStatus(status);
            reservationRepository.save(reservation.get());
        }
    }

    public void deleteReservation(UUID id) {
        reservationRepository.deleteById(id);
    }
}
