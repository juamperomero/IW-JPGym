package com.example.application;

import com.example.application.data.User;
import com.example.application.service.UserManagementService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.Serial;

@PageTitle("Registrate User")
@Route(value = "register")
@AnonymousAllowed
@CssImport("./styles/shared-styles.css")  // Archivo CSS para estilos personalizados
public class RegisterView extends VerticalLayout {

    @Serial
    private static final long serialVersionUID = 851217309689685413L;

    private final UserManagementService service;

    private final H1 title;

    private final TextField username;
    private final EmailField email;
    private final PasswordField password;
    private final PasswordField password2;

    private final Button register;
    private final H4 status;

    private final BeanValidationBinder<User> binder;

    public RegisterView(UserManagementService service) {
        this.service = service;

        // Añadir clase CSS personalizada
        addClassName("register-view");

        // Crear los campos de entrada
        title = new H1("Registrar usuario");
        username = new TextField("Nombre");
        email = new EmailField("Email");
        password = new PasswordField("Contraseña");
        password2 = new PasswordField("Repite tu contraseña");

        // Crear el botón de registro
        register = new Button("Registrar");
        register.addClassName("register-button");  // Añadir clase CSS personalizada

        // Crear el mensaje de estado
        status = new H4();
        status.setVisible(false);

        // Estilizar y encapsular en un Div (tarjeta)
        Div card = new Div();
        card.addClassName("register-card");
        card.add(title, username, email, password, password2, register, status);

        // Crear un layout para centrar la tarjeta
        VerticalLayout layout = new VerticalLayout(card);
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setSizeFull();  // Asegurar que ocupe toda la pantalla

        // Añadir el layout a la vista
        add(layout);

        // Configuración del binder
        binder = new BeanValidationBinder<>(User.class);
        binder.bindInstanceFields(this);
        binder.setBean(new User());

        // Evento del botón de registro
        register.addClickListener(e -> onRegisterButtonClick());
    }

    /**
     * Handler
     */
    public void onRegisterButtonClick() {
        if (binder.validate().isOk() && password.getValue().equals(password2.getValue())) {
            if (service.registerUser(binder.getBean())) {
                status.setText("Excelente. ¡Por favor revisa tu bandeja de entrada de correo electrónico!");
                status.setVisible(true);
                binder.setBean(new User());
                password2.setValue("");
            } else {
                Notification.show("El nombre está ya en uso");
            }
        } else {
            Notification.show("Por favor, verifique los datos de entrada.");
        }
    }
}