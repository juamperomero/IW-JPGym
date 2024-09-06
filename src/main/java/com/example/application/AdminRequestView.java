package com.example.application;

import com.example.application.data.Reservation;
import com.example.application.data.ReservationStatus;
import com.example.application.service.EmailService;
import com.example.application.service.ReservationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route(value = "admin/requests", layout = MainLayout.class)
@PageTitle("Solicitudes de Reserva")
@RolesAllowed("ADMIN")
public class AdminRequestView extends VerticalLayout {

    private final ReservationService reservationService;
    private final EmailService emailService;
    private final Grid<Reservation> grid;

    @Autowired
    public AdminRequestView(ReservationService reservationService, EmailService emailService) {
        this.reservationService = reservationService;
        this.emailService = emailService;
        this.grid = new Grid<>(Reservation.class);

        List<Reservation> pendingReservations = reservationService.findReservationsByStatus(ReservationStatus.PENDIENTE);
        grid.setItems(pendingReservations);

        // Configurar columnas explícitamente
        grid.removeAllColumns();
        grid.addColumn(reservation -> reservation.getClassEntity().getName())
                .setHeader("Clase");
        grid.addColumn(reservation -> reservation.getUser().getUsername())
                .setHeader("Usuario");
        grid.addColumn(reservation -> reservation.getClassEntity().getSchedule().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                .setHeader("Hora de Clase");
        grid.addColumn(Reservation::getStatus)
                .setHeader("Estado");

        // Añadir botones de confirmar y cancelar
        grid.addComponentColumn(this::createConfirmButton).setHeader("Confirmar");
        grid.addComponentColumn(this::createCancelButton).setHeader("Cancelar");

        add(grid);
        setSizeFull();
    }

    private Button createConfirmButton(Reservation reservation) {
        Button confirmButton = new Button("Confirmar");
        confirmButton.addClickListener(e -> confirmReservation(reservation));
        return confirmButton;
    }

    private Button createCancelButton(Reservation reservation) {
        Button cancelButton = new Button("Cancelar");
        cancelButton.addClickListener(e -> cancelReservation(reservation));
        return cancelButton;
    }

    @Transactional
    private void confirmReservation(Reservation reservation) {
        // Verificar si la capacidad de la clase permite confirmar la reserva
        long confirmedReservations = reservationService.findReservationsByClassEntityAndStatus(reservation.getClassEntity(), ReservationStatus.CONFIRMADA).size();
        if (confirmedReservations >= reservation.getClassEntity().getCapacity()) {
            Notification.show("La clase ya está llena.");
            return;
        }

        reservation.setStatus(ReservationStatus.CONFIRMADA);
        reservationService.saveReservation(reservation);
        emailService.sendReservationStatusEmail(reservation);
        Notification.show("Reserva confirmada.");
        refreshGrid();
    }

    @Transactional
    private void cancelReservation(Reservation reservation) {
        reservation.setStatus(ReservationStatus.CANCELADA);
        reservationService.saveReservation(reservation);
        emailService.sendReservationStatusEmail(reservation);
        Notification.show("Reserva cancelada.");
        refreshGrid();
    }

    private void refreshGrid() {
        List<Reservation> pendingReservations = reservationService.findReservationsByStatus(ReservationStatus.PENDIENTE);
        grid.setItems(pendingReservations);
    }
}