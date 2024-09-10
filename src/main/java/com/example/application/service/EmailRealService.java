package com.example.application.service;

import com.example.application.data.Reservation;
import com.example.application.data.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;

@Service
public class EmailRealService implements EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailRealService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String defaultMail;

    @Value("${server.url}")
    private String serverUrl;

    public EmailRealService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public boolean sendRegistrationEmail(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "¡Bienvenido!";
        String body = "Debes activar tu cuenta. "
                + "Ingresa en " + serverUrl + "useractivation?email=" + user.getEmail() + "&code=" + user.getRegisterCode()
                + " e introduce tu mail y el siguiente código: "
                + user.getRegisterCode();

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
            logger.info("Correo de registro enviado a: " + user.getEmail());
        } catch (MailException | MessagingException ex) {
            logger.error("Error al enviar el correo de registro a: " + user.getEmail(), ex);
            return false;
        }

        return true;
    }

    @Override
    public boolean sendReservationStatusEmail(Reservation reservation) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Petición de reserva JPGym";
        String body = "Tu petición para la clase de " + reservation.getClassEntity().getName() +
                " ha sido " + reservation.getStatus().toString().toLowerCase() + ".";

        try {
            helper.setFrom(defaultMail);
            helper.setTo(reservation.getUser().getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
            logger.info("Correo de estado de reserva enviado a: " + reservation.getUser().getEmail());
        } catch (MailException | MessagingException ex) {
            logger.error("Error al enviar el correo de estado de reserva a: " + reservation.getUser().getEmail(), ex);
            return false;
        }

        return true;
    }

    @Override
    public boolean sendClassReminderEmail(Reservation reservation) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "Recordatorio de tu clase JPGym";
        String body = "Este correo es un recordatorio para tu clase " + reservation.getClassEntity().getName() +
                " mañana a la fecha y hora de " + reservation.getClassEntity().getSchedule().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")) + ".";

        try {
            helper.setFrom(defaultMail);
            helper.setTo(reservation.getUser().getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
            logger.info("Correo de recordatorio enviado a: " + reservation.getUser().getEmail());
        } catch (MailException | MessagingException ex) {
            logger.error("Error al enviar el correo de recordatorio a: " + reservation.getUser().getEmail(), ex);
            return false;
        }

        return true;
    }
}