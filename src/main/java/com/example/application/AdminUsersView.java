package com.example.application;

import com.example.application.data.User;
import com.example.application.service.UserManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Gestionar Usuarios")
@Route(value = "admin/users", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdminUsersView extends VerticalLayout {

    private final UserManagementService userManagementService;
    private final Grid<User> grid;
    private final TextField usernameField;
    private final TextField emailField;
    private final Button saveButton;
    private final Button deleteButton;
    private User selectedUser;

    @Autowired
    public AdminUsersView(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
        this.grid = new Grid<>(User.class);
        this.usernameField = new TextField("Nombre de Usuario");
        this.emailField = new TextField("Correo Electrónico");
        this.saveButton = new Button("Guardar");
        this.deleteButton = new Button("Eliminar");

        configureGrid();
        configureForm();

        add(grid, new HorizontalLayout(usernameField, emailField, saveButton, deleteButton));
        setSizeFull();
        refreshGrid();
    }

    private void configureGrid() {
        grid.setColumns("id", "username", "email");
        grid.getColumnByKey("id").setHeader("ID");
        grid.getColumnByKey("username").setHeader("Nombre de Usuario");
        grid.getColumnByKey("email").setHeader("Correo Electrónico");

        grid.asSingleSelect().addValueChangeListener(event -> {
            selectedUser = event.getValue();
            if (selectedUser != null) {
                usernameField.setValue(selectedUser.getUsername());
                emailField.setValue(selectedUser.getEmail());
            } else {
                clearForm();
            }
        });
    }

    private void configureForm() {
        saveButton.addClickListener(e -> saveUser());
        deleteButton.addClickListener(e -> deleteUser());
    }

    private void saveUser() {
        if (selectedUser != null) {
            selectedUser.setUsername(usernameField.getValue());
            selectedUser.setEmail(emailField.getValue());
            userManagementService.saveUser(selectedUser);
            Notification.show("Usuario guardado.");
            refreshGrid();
            clearForm();
        }
    }

    private void deleteUser() {
        if (selectedUser != null) {
            userManagementService.deleteUser(selectedUser.getId());
            Notification.show("Usuario eliminado.");
            refreshGrid();
            clearForm();
        }
    }

    private void refreshGrid() {
        grid.setItems(userManagementService.findAllUsers());
    }

    private void clearForm() {
        selectedUser = null;
        usernameField.clear();
        emailField.clear();
    }
}
