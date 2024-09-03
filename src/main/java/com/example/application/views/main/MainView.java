package com.example.application.views.main;

import com.example.application.*;
import com.example.application.data.User;
import com.example.application.data.Role;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@PageTitle("Inicio")
@Route(value = "home", layout = MainLayout.class)
@CssImport("./styles/shared-styles.css")
@PermitAll
public class MainView extends AppLayout{

  private final AuthenticatedUser authenticatedUser;

  public MainView(AuthenticatedUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
    createHeader();
  }

  private void createHeader() {
    H1 title = new H1("JPGym");
    title.getStyle().set("margin", "0");

    Optional<User> maybeUser = authenticatedUser.get();
    String username = maybeUser.map(User::getUsername).orElse("Usuario");

    Span userInfo = new Span("Bienvenido, " + username);



    HorizontalLayout header = new HorizontalLayout(title, userInfo);
    header.expand(title);
    header.setWidth("100%");
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.setPadding(true);
    header.setSpacing(true);
    header.getStyle().set("background-color", "#f3f4f6");

    addToNavbar(header);
  }

}