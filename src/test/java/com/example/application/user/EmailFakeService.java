package com.example.application.user;



import com.example.application.data.Reservation;
import com.example.application.data.User;
import com.example.application.service.EmailService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class EmailFakeService implements EmailService {

    @Override
    public boolean sendRegistrationEmail(User user) {

        String subject = "Welcome";
        String body = "You should active your account. "
                + "Go to http://localhost:8080/useractivation "
                + "and introduce your mail and the following code: "
                + user.getRegisterCode();

        try {
            System.out.println("From: app (testing)");
            System.out.println("To: " + user.getEmail());
            System.out.println("Subject: " + subject);
            System.out.println("Body: " + body);

            int secondsToSleep = 5;
            Thread.sleep(secondsToSleep * 1000);
            System.out.println("Email send simulation done!");
            return true;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public boolean sendReservationStatusEmail(Reservation reservation) {
        // Implementación ficticia del método
        System.out.println("Enviando correo de estado de reserva a " + reservation.getUser().getEmail());
        // Simular el envío de correo y devolver true si tiene éxito
        return true;
    }

    @Override
    public boolean sendClassReminderEmail(Reservation reservation) {
        // Implementación ficticia del método
        System.out.println("Enviando recordatorio de clase a " + reservation.getUser().getEmail());
        // Simular el envío de correo y devolver true si tiene éxito
        return true;
    }
}
