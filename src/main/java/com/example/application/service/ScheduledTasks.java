package com.example.application.service;

import com.example.application.data.Reservation;
import com.example.application.data.ReservationStatus;
import com.example.application.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTasks {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final ReservationRepository reservationRepository;
    private final EmailService emailService;

    @Autowired
    public ScheduledTasks(ReservationRepository reservationRepository, EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 * * * * ?") // Ejecutar cada minuto para pruebas
    @Transactional
    public void sendClassReminders() {
        logger.info("Tarea programada ejecutada.");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(24);
        logger.info("Ejecutando tarea programada para enviar recordatorios de clases.");

        // Obtener todas las reservas confirmadas que no tienen el recordatorio enviado
        List<Reservation> reservations = reservationRepository.findByStatusAndReminderSentFalse(ReservationStatus.CONFIRMADA);
        logger.info("Reservas encontradas: " + reservations.size());

        for (Reservation reservation : reservations) {
            LocalDateTime classStartTime = reservation.getClassEntity().getSchedule();
            // Verificar si la clase empieza en 24 horas
            if (classStartTime.isAfter(now) && classStartTime.isBefore(now.plusHours(24))) {
                logger.info("Reserva encontrada: " + reservation.getId() + ", Clase: " + reservation.getClassEntity().getName() + ", Fecha y Hora: " + classStartTime);
                boolean emailSent = emailService.sendClassReminderEmail(reservation);
                if (emailSent) {
                    logger.info("Correo enviado exitosamente para la reserva: " + reservation.getId());
                    // Marcar el recordatorio como enviado
                    reservation.setReminderSent(true);
                    reservationRepository.save(reservation);
                } else {
                    logger.error("Error al enviar el correo para la reserva: " + reservation.getId());
                }
            }
        }
    }
}