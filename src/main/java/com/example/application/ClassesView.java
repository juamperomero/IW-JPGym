package com.example.application;

import com.example.application.data.ClassEntity;
import com.example.application.data.Reservation;
import com.example.application.data.ReservationStatus;
import com.example.application.data.User;
import com.example.application.service.ClassService;
import com.example.application.service.ReservationService;
import com.example.application.service.UserManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Route(value = "classes", layout = MainLayout.class)
@PageTitle("Clases")
@PermitAll
public class ClassesView extends VerticalLayout {

    private final ClassService classService;
    private final ReservationService reservationService;
    private final UserManagementService userService;
    private final Grid<ClassEntity> grid;

    @Autowired
    public ClassesView(ClassService classService, ReservationService reservationService, UserManagementService userService) {
        this.classService = classService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.grid = new Grid<>(ClassEntity.class);

        List<ClassEntity> classes = classService.findAllClasses();
        grid.setItems(classes);

        // Definir los nombres de las columnas en español
        grid.setColumns("name", "description", "schedule", "instructor.name", "capacity");
        grid.getColumnByKey("name").setHeader("Nombre");
        grid.getColumnByKey("description").setHeader("Descripción");
        grid.getColumnByKey("schedule").setHeader("Horario");
        grid.getColumnByKey("instructor.name").setHeader("Instructor");
        grid.getColumnByKey("capacity").setHeader("Capacidad");

        // Añadir botón de reserva
        grid.addComponentColumn(this::createReserveButton).setHeader("Reservar");

        add(grid);
        setSizeFull();
    }

    private Button createReserveButton(ClassEntity classEntity) {
        Button reserveButton = new Button("Reservar");
        reserveButton.addClickListener(e -> reserveClass(classEntity));
        return reserveButton;
    }

    @Transactional
    private void reserveClass(ClassEntity classEntity) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.loadUserByUsername(username);

        // Carga la clase con la lista de asistentes para evitar LazyInitializationException
        classEntity = classService.findById(classEntity.getId());

        // Validar si el usuario ya tiene una reserva para la misma clase
        if (userHasReservationForClass(user, classEntity)) {
            Notification.show("Ya tienes una reserva para esta clase.");
            return;
        }

        if (classEntity.getCapacity() > classEntity.getAttendees().size()) {
            Reservation reservation = new Reservation();
            reservation.setClassEntity(classEntity);
            reservation.setUser(user);
            reservation.setStatus(ReservationStatus.PENDIENTE); // Estado de reserva pendiente
            reservationService.saveReservation(reservation);

            classEntity.getAttendees().add(user);
            classService.saveClass(classEntity);

            Notification.show("Reserva realizada con éxito.");
        } else {
            Notification.show("La clase está llena.");
        }
    }

    private boolean userHasReservationForClass(User user, ClassEntity classEntity) {
        List<Reservation> userReservations = reservationService.findReservationsByUser(user);
        return userReservations.stream()
                .anyMatch(reservation -> reservation.getClassEntity().getId().equals(classEntity.getId()));
    }
}