package com.example.application;

import com.example.application.data.Reservation;
import com.example.application.data.User;
import com.example.application.service.ReservationService;
import com.example.application.service.UserManagementService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "user/reservations", layout = MainLayout.class)
@PageTitle("Mis Reservas")
@PermitAll
public class UserReservationView extends VerticalLayout {

    private final ReservationService reservationService;
    private final UserManagementService userService;
    private final Grid<Reservation> grid;

    @Autowired
    public UserReservationView(ReservationService reservationService, UserManagementService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.grid = new Grid<>(Reservation.class);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.loadUserByUsername(username);

        List<Reservation> reservations = reservationService.findAllReservations().stream()
                .filter(reservation -> reservation.getUser().equals(user))
                .collect(Collectors.toList());

        grid.setItems(reservations);
        // Definir los nombres de las columnas en español
        grid.setColumns("classEntity.name", "reservationTime", "status");
        grid.getColumnByKey("classEntity.name").setHeader("Clase");
        grid.getColumnByKey("reservationTime").setHeader("Hora de Reserva");
        grid.getColumnByKey("status").setHeader("Estado");

        add(grid);
        setSizeFull();
    }
}
