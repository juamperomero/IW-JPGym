package com.example.application;


import com.example.application.data.Instructor;
import com.example.application.service.InstructorService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "instructors", layout = MainLayout.class)
@PageTitle("Instructores")
@PermitAll
public class InstructorsView extends VerticalLayout {

    private final InstructorService instructorService;
    private final Grid<Instructor> grid;

    @Autowired
    public InstructorsView(InstructorService instructorService) {
        this.instructorService = instructorService;
        this.grid = new Grid<>(Instructor.class);

        List<Instructor> instructors = instructorService.findAllInstructors();
        grid.setItems(instructors);

        grid.setColumns("name");

        add(grid);
        setSizeFull();
    }
}
