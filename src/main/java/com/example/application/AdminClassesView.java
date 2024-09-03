package com.example.application;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;

@PageTitle("Gestionar Clases")
@Route(value = "admin/classes", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AdminClassesView extends VerticalLayout {
}
