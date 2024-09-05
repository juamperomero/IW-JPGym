package com.example.application;

import com.example.application.data.Reservation;
import com.example.application.data.User;
import com.example.application.service.ReservationService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = "user/reservations", layout = MainLayout.class)
@PageTitle("Mis Clases")
@PermitAll
public class UserReservationView extends VerticalLayout {

    private final ReservationService reservationService;
    private final AuthenticatedUser authenticatedUser;
    private final Grid<Reservation> grid;

    @Autowired
    public UserReservationView(ReservationService reservationService, AuthenticatedUser authenticatedUser) {
        this.reservationService = reservationService;
        this.authenticatedUser = authenticatedUser;
        this.grid = new Grid<>(Reservation.class);

        // Utiliza AuthenticatedUser para obtener el usuario actual
        Optional<User> userOptional = authenticatedUser.get();
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Obtener las reservas del usuario
            List<Reservation> reservations = reservationService.findReservationsByUser(user);

            grid.setItems(reservations);

            grid.addColumn(reservation -> reservation.getClassEntity().getName())
                    .setHeader("Clase");

            grid.addColumn(reservation -> reservation.getReservationTime()
                            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                    .setHeader("Hora de Reserva");

            grid.addColumn(Reservation::getStatus)
                    .setHeader("Estado");

        } else {
            grid.setItems(List.of()); // Sin reservas si el usuario no está autenticado
        }

        add(grid);
        setSizeFull();
    }
}
