package com.example.application;

import com.example.application.data.ClassEntity;
import com.example.application.data.Reservation;
import com.example.application.data.User;
import com.example.application.service.ReservationService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Route(value = "user/reservations", layout = MainLayout.class)
@PageTitle("Mis clases")
@PermitAll
public class UserReservationView extends VerticalLayout {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserReservationView.class);

    private final ReservationService reservationService;
    private final AuthenticatedUser authenticatedUser;
    private final Grid<Reservation> grid;

    @Autowired
    public UserReservationView(ReservationService reservationService, AuthenticatedUser authenticatedUser) {
        this.reservationService = reservationService;
        this.authenticatedUser = authenticatedUser;
        this.grid = new Grid<>();

        Optional<User> userOptional = authenticatedUser.get();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            LOGGER.info("Usuario autenticado: {}", user);

            List<Reservation> reservations = getUserReservations(user);
            LOGGER.info("Reservas encontradas: {}", reservations);

            grid.setItems(reservations);

            // Columna para mostrar el nombre de la clase reservada
            grid.addColumn(reservation -> {
                        ClassEntity classEntity = reservation.getClassEntity();
                        return classEntity != null ? classEntity.getName() : "Clase no disponible";
                    })
                    .setHeader("Clase");

            // Columna para mostrar la hora de inicio de la clase formateada
            grid.addColumn(reservation -> {
                        ClassEntity classEntity = reservation.getClassEntity();
                        return classEntity != null ? classEntity.getSchedule().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")) : "Hora no disponible";
                    })
                    .setHeader("Hora de Inicio");

            // Columna para mostrar el estado de la reserva
            grid.addColumn(Reservation::getStatus)
                    .setHeader("Estado");

        } else {
            grid.setItems(List.of());
        }

        add(grid);
        setSizeFull();
    }

    @Transactional
    public List<Reservation> getUserReservations(User user) {
        List<Reservation> reservations = reservationService.findReservationsByUser(user);
        reservations.forEach(reservation -> {
            Hibernate.initialize(reservation.getClassEntity()); // Inicializa la entidad
            Hibernate.initialize(reservation.getUser()); // Inicializa la entidad User si es necesario
        });
        LOGGER.info("Reservas obtenidas del servicio: {}", reservations);
        return reservations;
    }
}