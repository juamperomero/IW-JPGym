package com.example.application;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.router.internal.RouteUtil;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@PageTitle("Login")
@Route(value = "login")
@CssImport("./styles/shared-styles.css")  // Archivo CSS para estilos personalizados
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final AuthenticatedUser authenticatedUser;
    private final LoginForm loginForm;

    public LoginView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        // Añadir clase CSS personalizada
        addClassName("login-view");

        // Crear el LoginForm
        loginForm = new LoginForm();
        loginForm.setAction(RouteUtil.getRoutePath(VaadinService.getCurrent().getContext(), getClass()));

        // Configurar la internacionalización (i18n)
        LoginI18n i18n = LoginI18n.createDefault();

        // Asegurarse de que el Header no sea nulo
        if (i18n.getHeader() == null) {
            i18n.setHeader(new LoginI18n.Header());
        }

        i18n.getHeader().setTitle("JPGym");
        i18n.getForm().setForgotPassword("");  // Ocultar el texto "Forgot password"
        loginForm.setI18n(i18n);

        // Ocultar el botón "Forgot password"
        loginForm.setForgotPasswordButtonVisible(false);

        // Crear el botón de registro
        Button registerButton = new Button("Registrate", event -> {
            // Redirigir a la ruta de registro
            getUI().ifPresent(ui -> ui.navigate("register"));
        });
        registerButton.addClassName("register-button");  // Añadir clase CSS personalizada

        // Crear un contenedor estilizado (tarjeta)
        Div card = new Div();
        card.addClassName("login-card");
        card.add(loginForm, registerButton);  // Añadir el formulario de login y el botón de registro al mismo contenedor

        // Crear un layout para centrar la tarjeta
        VerticalLayout layout = new VerticalLayout(card);
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setSizeFull();  // Asegurar que ocupe toda la pantalla

        // Añadir el layout a la vista
        add(layout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (authenticatedUser.get().isPresent()) {
            // Si ya está logueado
            event.forwardTo("");
        }

        loginForm.setError(event.getLocation().getQueryParameters().getParameters().containsKey("error"));
    }
}