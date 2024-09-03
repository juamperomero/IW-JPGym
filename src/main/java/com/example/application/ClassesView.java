package com.example.application;


import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "classes", layout = MainLayout.class)
@PageTitle("Clases")
@PermitAll
public class ClassesView extends VerticalLayout {
}