package com.example.application.service;

import com.example.application.data.Reservation;
import com.example.application.data.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.time.format.DateTimeFormatter;

@Service
public class EmailRealService implements EmailService {
    private final JavaMailSender mailSender;
    private final HttpServletRequest request;

    @Value("${spring.mail.username}")
    private String defaultMail;


    public EmailRealService(JavaMailSender mailSender, HttpServletRequest request) {
        this.mailSender = mailSender;
        this.request = request;
    }


    private String getServerUrl() {

        String serverUrl = request.getScheme() + "://" + request.getServerName();
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            serverUrl += ":" + request.getServerPort();
        }
        serverUrl += "/";
        return serverUrl;

    }

    @Override
    public boolean sendRegistrationEmail(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

        String subject = "¡Bienvenido!";
        String body = "Debes activar tu cuenta. "
                + "ve a " + getServerUrl() + "useractivation "
                + "y entroduce tu mail y el siguiente codigo: "
                + user.getRegisterCode();

        try {
            helper.setFrom(defaultMail);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(body);
            this.mailSender.send(message);
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
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
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
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
        } catch (MailException | MessagingException ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }
}