package com.example.application.service;

import com.example.application.data.Reservation;
import com.example.application.data.ReservationStatus;
import com.example.application.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTasks {

    private final ReservationRepository reservationRepository;
    private final EmailService emailService;

    @Autowired
    public ScheduledTasks(ReservationRepository reservationRepository, EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 * * * ?") // Ejecutar cada hora
    @Transactional
    public void sendClassReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(24);
        List<Reservation> reservations = reservationRepository.findByClassEntity_ScheduleBetweenAndStatus(reminderTime.minusMinutes(1), reminderTime.plusMinutes(1), ReservationStatus.CONFIRMADA);
        for (Reservation reservation : reservations) {
            emailService.sendClassReminderEmail(reservation);
        }
    }
}