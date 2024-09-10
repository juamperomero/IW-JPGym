package com.example.application.views.main;

import com.example.application.AuthenticatedUser;
import com.example.application.MainLayout;
import com.example.application.data.User;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@PageTitle("Inicio")
@Route(value = "", layout = MainLayout.class)
@CssImport("./styles/shared-styles.css")
@PermitAll
public class MainView extends AppLayout {

  private final AuthenticatedUser authenticatedUser;

  public MainView(AuthenticatedUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;
    createHeader();
    createContent();
  }

  private void createHeader() {
    H1 title = new H1("JPGym");
    title.getStyle().set("margin", "0");

    Optional<User> maybeUser = authenticatedUser.get();

    HorizontalLayout header = new HorizontalLayout(title);
    header.expand(title);
    header.setWidth("100%");
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.setPadding(true);
    header.setSpacing(true);
    header.getStyle().set("background-color", "#f3f4f6");

    addToNavbar(header);
  }

  private void createContent() {
    // Mensaje de bienvenida
    H2 welcomeMessage = new H2("Hola " + authenticatedUser.get().map(User::getUsername).orElse("Usuario") + ". ¡Bienvenido a JPGym!");
    welcomeMessage.addClassName("welcome-message");

    // Panel de información
    Div infoPanel = new Div();
    infoPanel.addClassName("info-panel");
    infoPanel.add(new Paragraph("Horarios de lunes a sábados de 10:00 a 21:00"));

    // Contenedor para el mensaje "Reserva tu clase"
    Div reserveMessageContainer = new Div();
    reserveMessageContainer.addClassName("reserve-message-container");
    reserveMessageContainer.add(new Paragraph("Reserva tu clase"));

    // Navegación rápida
    HorizontalLayout quickNav = new HorizontalLayout();
    quickNav.addClassName("quick-nav");
    Button viewClassesButton = new Button("Ver Clases", event -> getUI().ifPresent(ui -> ui.navigate("classes")));
    quickNav.add(viewClassesButton);

    // Tarjetas de información
    HorizontalLayout infoCards = new HorizontalLayout();
    infoCards.addClassName("info-cards");

    // Layout principal
    VerticalLayout mainContent = new VerticalLayout(welcomeMessage, infoPanel, reserveMessageContainer, quickNav, infoCards);
    mainContent.setAlignItems(FlexComponent.Alignment.CENTER);
    mainContent.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    mainContent.setSpacing(true);
    mainContent.setPadding(true);

    setContent(mainContent);
  }

  private Div createInfoCard(String title, String value) {
    Div card = new Div();
    card.addClassName("info-card");

    H2 cardTitle = new H2(title);
    cardTitle.addClassName("card-title");

    H1 cardValue = new H1(value);
    cardValue.addClassName("card-value");

    card.add(cardTitle, cardValue);
    return card;
  }
}