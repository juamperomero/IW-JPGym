package com.example.application;


import com.example.application.data.ClassEntity;
import com.example.application.data.Instructor;
import com.example.application.service.ClassService;
import com.example.application.service.InstructorService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@PageTitle("Gestionar Clases")
@Route(value = "admin/classes", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdminClassesView extends VerticalLayout {

    private final ClassService classService;
    private final InstructorService instructorService;

    private final Grid<ClassEntity> grid = new Grid<>(ClassEntity.class);
    private final TextField nameField = new TextField("Nombre de la Clase");
    private final TextArea descriptionField = new TextArea("Descripción");
    private final TextField scheduleField = new TextField("Horario (YYYY-MM-DD HH:MM)");
    private final NumberField capacityField = new NumberField("Capacidad");
    private final ComboBox<Instructor> instructorField = new ComboBox<>("Instructor");

    private final Button saveButton = new Button("Guardar");
    private final Button deleteButton = new Button("Eliminar");
    private final Button cancelButton = new Button("Cancelar");

    private final Binder<ClassEntity> binder = new Binder<>(ClassEntity.class);

    private ClassEntity currentClassEntity;

    @Autowired
    public AdminClassesView(ClassService classService, InstructorService instructorService) {
        this.classService = classService;
        this.instructorService = instructorService;

        setSizeFull();
        configureGrid();
        configureForm();

        HorizontalLayout formLayout = new HorizontalLayout(nameField, descriptionField, scheduleField, capacityField, instructorField);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, deleteButton, cancelButton);

        VerticalLayout content = new VerticalLayout(grid, formLayout, buttonLayout);
        content.setSizeFull();

        add(content);

        updateGrid();
        clearForm();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("name", "description", "schedule", "capacity", "instructor.name");
        grid.getColumnByKey("name").setHeader("Nombre");
        grid.getColumnByKey("description").setHeader("Descripción");
        grid.getColumnByKey("schedule").setHeader("Horario");
        grid.getColumnByKey("capacity").setHeader("Capacidad");
        grid.getColumnByKey("instructor.name").setHeader("Instructor");

        grid.asSingleSelect().addValueChangeListener(event -> editClass(event.getValue()));
    }

    private void configureForm() {
        binder.forField(nameField).bind(ClassEntity::getName, ClassEntity::setName);
        binder.forField(descriptionField).bind(ClassEntity::getDescription, ClassEntity::setDescription);

        // Convertidor personalizado para LocalDateTime
        binder.forField(scheduleField)
                .withConverter(new LocalDateTimeConverter())
                .bind(ClassEntity::getSchedule, ClassEntity::setSchedule);

        binder.forField(capacityField)
                .withConverter(new DoubleToIntegerConverter())
                .bind(ClassEntity::getCapacity, ClassEntity::setCapacity);

        binder.forField(instructorField).bind(ClassEntity::getInstructor, ClassEntity::setInstructor);

        List<Instructor> instructors = instructorService.findAllInstructors();
        instructorField.setItems(instructors);
        instructorField.setItemLabelGenerator(Instructor::getName);

        saveButton.addClickListener(e -> saveClass());
        deleteButton.addClickListener(e -> deleteClass());
        cancelButton.addClickListener(e -> clearForm());
    }

    private void updateGrid() {
        grid.setItems(classService.findAllClasses());
    }

    private void clearForm() {
        binder.setBean(null);
        nameField.clear();
        descriptionField.clear();
        scheduleField.clear();
        capacityField.clear();
        instructorField.clear();
        currentClassEntity = null;
        saveButton.setText("Guardar");
        deleteButton.setEnabled(false);
    }

    private void editClass(ClassEntity classEntity) {
        if (classEntity == null) {
            clearForm();
        } else {
            currentClassEntity = classEntity;
            binder.setBean(classEntity);
            saveButton.setText("Actualizar");
            deleteButton.setEnabled(true);
        }
    }

    private void saveClass() {
        if (currentClassEntity == null) {
            currentClassEntity = new ClassEntity();
        }

        try {
            binder.validate();
            if (binder.isValid()) {
                binder.writeBean(currentClassEntity);
                classService.saveClass(currentClassEntity);
                updateGrid();
                clearForm();
                Notification.show("Clase guardada", 3000, Notification.Position.BOTTOM_START);
            } else {
                Notification.show("Hay errores en el formulario. Por favor revisa los campos.", 3000, Notification.Position.BOTTOM_START);
            }
        } catch (ValidationException e) {
            Notification.show("No se pudo guardar la clase, por favor revisa los campos", 3000, Notification.Position.BOTTOM_START);
        } catch (Exception e) {
            Notification.show("Error al guardar la clase: " + e.getMessage(), 5000, Notification.Position.BOTTOM_START);
        }
    }

    private void deleteClass() {
        if (currentClassEntity != null) {
            classService.deleteClass(currentClassEntity.getId());
            updateGrid();
            clearForm();
            Notification.show("Clase eliminada", 3000, Notification.Position.BOTTOM_START);
        }
    }

    // Convertidor personalizado para LocalDateTime
    public static class LocalDateTimeConverter implements Converter<String, LocalDateTime> {
        private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        @Override
        public Result<LocalDateTime> convertToModel(String value, ValueContext context) {
            try {
                return Result.ok(LocalDateTime.parse(value, formatter));
            } catch (Exception e) {
                return Result.error("Formato de fecha/hora incorrecto");
            }
        }

        @Override
        public String convertToPresentation(LocalDateTime value, ValueContext context) {
            return value != null ? value.format(formatter) : "";
        }
    }

    // Convertidor de Double a Integer para capacidad
    public static class DoubleToIntegerConverter implements Converter<Double, Integer> {
        @Override
        public Result<Integer> convertToModel(Double value, ValueContext context) {
            return value != null ? Result.ok(value.intValue()) : Result.error("Debe ser un número entero");
        }

        @Override
        public Double convertToPresentation(Integer value, ValueContext context) {
            return value != null ? value.doubleValue() : null;
        }
    }
}
