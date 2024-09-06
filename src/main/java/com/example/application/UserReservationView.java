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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Route(value = "user/reservations", layout = MainLayout.class)
@PageTitle("Mis Reservas")
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

            // Asegúrate de obtener las reservas en un contexto transaccional
            List<Reservation> reservations = getUserReservations(user);

            // Configurar el grid para mostrar las reservas del usuario
            grid.setItems(reservations);

            // Columna para mostrar el nombre de la clase reservada
            grid.addColumn(reservation -> {
                        ClassEntity classEntity = reservation.getClassEntity();
                        return classEntity != null ? classEntity.getName() : "Clase no disponible";
                    })
                    .setHeader("Clase");

            // Columna para mostrar la hora de la reserva formateada
            grid.addColumn(reservation -> reservation.getReservationTime()
                            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                    .setHeader("Hora de Reserva");

            // Columna para mostrar el estado de la reserva
            grid.addColumn(Reservation::getStatus)
                    .setHeader("Estado");

        } else {
            // Si el usuario no está autenticado, no hay reservas que mostrar
            grid.setItems(List.of());
        }

        // Añadir el grid al layout
        add(grid);
        setSizeFull();
    }

    /**
     * Este método se asegura de cargar las reservas del usuario dentro de un contexto transaccional.
     */
    @Transactional
    public List<Reservation> getUserReservations(User user) {
        // Usamos el servicio de reservas para obtener todas las reservas del usuario
        return reservationService.findReservationsByUser(user);
    }
}
