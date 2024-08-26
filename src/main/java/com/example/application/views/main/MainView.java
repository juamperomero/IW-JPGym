package com.example.application.views.main;

import com.example.application.*;
import com.example.application.data.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@PageTitle("Inicio")
@Route("home")
@CssImport("./styles/shared-styles.css")
@PermitAll
public class MainView extends AppLayout implements AfterNavigationObserver {

  private final AuthenticatedUser authenticatedUser;

  public MainView(AuthenticatedUser authenticatedUser) {
    this.authenticatedUser = authenticatedUser;

  }

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    createHeader();
  }

  private void createHeader() {
    H1 title = new H1("JPGym");
    title.getStyle().set("margin", "0");

    Button logoutButton = new Button("Cerrar sesión", e -> {
      VaadinSession.getCurrent().getSession().invalidate();
      UI.getCurrent().getPage().setLocation("/login");
    });

    HorizontalLayout header = new HorizontalLayout(title, logoutButton);
    header.expand(title);
    header.setWidth("100%");
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.setPadding(true);
    header.setSpacing(true);
    header.getStyle().set("background-color", "#f3f4f6");

    addToNavbar(header);
  }

}