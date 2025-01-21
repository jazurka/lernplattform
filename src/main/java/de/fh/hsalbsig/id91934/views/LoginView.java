package de.fh.hsalbsig.id91934.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.fh.hsalbsig.id91934.entities.User;
import de.fh.hsalbsig.id91934.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Login View used for logging in and registering.
 */
@PageTitle("Login")
@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  private final LoginOverlay loginOverlay = new LoginOverlay();
  private final LoginOverlay registerOverlay = new LoginOverlay();

  /**
   * The UI used for creating components.
   * 
   * <p>@param userService gets autowired for database operations
   */
  @Autowired
  public LoginView(UserService userService) {

    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    LoginI18n loginForm = LoginI18n.createDefault();

    LoginI18n.Form lf = loginForm.getForm();
    lf.setTitle("Log In");
    lf.setUsername("Username");
    lf.setPassword("Password");
    lf.setSubmit("Log In");
    lf.setForgotPassword("Back");
    loginForm.setForm(lf);

    loginOverlay.setI18n(loginForm);
    loginOverlay.setTitle("Lernplattform");
    loginOverlay.setDescription(null);
    loginOverlay.setAction("login");
    loginOverlay.setOpened(false);
    loginOverlay.addForgotPasswordListener(forgotPasswordEvent -> {
      loginOverlay.setOpened(false);
    });

    Button loginButton = new Button("Login");
    loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    loginButton.addClickListener(buttonClickEvent -> {
      loginOverlay.setOpened(true);
    });

    LoginI18n registerForm = LoginI18n.createDefault();

    LoginI18n.Form rf = registerForm.getForm();
    rf.setTitle("Register");
    rf.setUsername("Username");
    rf.setPassword("Password");
    rf.setSubmit("Register");
    rf.setForgotPassword("Back");
    registerForm.setForm(rf);

    registerOverlay.setI18n(registerForm);
    registerOverlay.setTitle("Lernplattform");
    registerOverlay.setDescription(null);
    registerOverlay.setOpened(false);
    registerOverlay.addForgotPasswordListener(forgotPasswordEvent -> {
      registerOverlay.setOpened(false);
    });
    registerOverlay.addLoginListener(loginEvent -> {
      if (userService.findByName(loginEvent.getUsername()) != null) {
        Notification notification =
            Notification.show("User already exists", 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        registerOverlay.setEnabled(true);
      } else {
        User user = new User(loginEvent.getUsername(), loginEvent.getPassword(), "ROLE_USER");
        userService.save(user);

        Notification notification =
            Notification.show("User created", 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        registerOverlay.setOpened(false);
      }
    });

    Button registerButton = new Button("Register");
    registerButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    registerButton.addClickListener(buttonClickEvent -> {
      registerOverlay.setOpened(true);
    });

    HorizontalLayout buttons = new HorizontalLayout(loginButton, registerButton);

    add(new H1("Lernplattform"), buttons, loginOverlay);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

    if (beforeEnterEvent.getLocation().getQueryParameters().getParameters().containsKey("error")) {
      loginOverlay.setOpened(true);
      loginOverlay.setError(true);
    }
  }

}
