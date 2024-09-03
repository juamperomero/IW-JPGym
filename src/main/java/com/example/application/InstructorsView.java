package com.example.application;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "instructors", layout = MainLayout.class)
@PageTitle("Instructores")
@PermitAll
public class InstructorsView extends VerticalLayout {
    // Contenido de la vista
}
