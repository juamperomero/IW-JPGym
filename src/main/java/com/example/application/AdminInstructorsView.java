package com.example.application;


import com.example.application.data.Instructor;
import com.example.application.service.InstructorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

@PageTitle("Gestionar Instructores")
@Route(value = "admin/instructors", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdminInstructorsView extends VerticalLayout {

    private final InstructorService instructorService;
    private final Grid<Instructor> grid;
    private final Button addButton;

    @Autowired
    public AdminInstructorsView(InstructorService instructorService) {
        this.instructorService = instructorService;
        this.grid = new Grid<>(Instructor.class);
        this.addButton = new Button("Añadir Instructor");

        List<Instructor> instructors = instructorService.findAllInstructors();
        grid.setItems(instructors);

        grid.setColumns("name");
        grid.addComponentColumn(instructor -> createEditButton(instructor)).setHeader("Acciones");

        addButton.addClickListener(e -> showInstructorForm(new Instructor()));

        add(addButton, grid);
        setSizeFull();
    }

    private Button createEditButton(Instructor instructor) {
        Button editButton = new Button("Editar");
        editButton.addClickListener(e -> showInstructorForm(instructor));
        return editButton;
    }

    private void showInstructorForm(Instructor instructor) {
        Dialog dialog = new Dialog();

        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Nombre");
        nameField.setValue(Optional.ofNullable(instructor.getName()).orElse(""));

        Binder<Instructor> binder = new Binder<>(Instructor.class);
        binder.forField(nameField).asRequired("El nombre es obligatorio").bind(Instructor::getName, Instructor::setName);

        formLayout.add(nameField);

        Button saveButton = new Button("Guardar", e -> {
            if (binder.writeBeanIfValid(instructor)) {
                instructorService.saveInstructor(instructor);
                grid.setItems(instructorService.findAllInstructors());
                Notification.show("Instructor guardado con éxito.");
                dialog.close();
            }
        });

        Button deleteButton = new Button("Eliminar", e -> {
            instructorService.deleteInstructor(instructor.getId());
            grid.setItems(instructorService.findAllInstructors());
            Notification.show("Instructor eliminado.");
            dialog.close();
        });
        deleteButton.setVisible(instructor.getId() != null);

        HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton);

        dialog.add(formLayout, buttons);
        dialog.open();
    }
}
