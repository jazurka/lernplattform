package de.fh.hsalbsig.id91934.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.fh.hsalbsig.id91934.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Form to handle selected users in the grid.
 */
public class UserForm extends FormLayout {

  private TextField name = new TextField("Name");
  private ComboBox<String> role = new ComboBox<>("Role");

  private final Button saveButton = new Button("Save");
  private final Button deleteButton = new Button("Delete");
  private final Button cancelButton = new Button("Cancel");

  private Binder<User> binder = new BeanValidationBinder<>(User.class);
  private User user;

  private static final Logger LOGGER = LogManager.getLogger();
  
  /**
   * The constructor setting up all the components.
   */
  public UserForm() {

    name.getStyle().set("padding", "0px");

    binder.bind(name, User::getName, User::setName);
    binder.bind(role, User::getRole, User::setRole);

    role.setItems("ROLE_USER", "ROLE_ADMIN", "ROLE_PROF");
    // role.setItemLabelGenerator(role -> role.substring(5).toLowerCase());

    add(name, role, createButtonLayout());
  }

  /**
   * Used to fill UI fields with user data.
   * 
   * <p>@param user that has been selected in the grid
   */
  public void setUser(User user) {
    this.user = user;
    binder.readBean(user);
  }

  private Component createButtonLayout() {
    saveButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    // cancelButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

    saveButton.addClickListener(event -> validateAndSave());
    deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, user)));
    cancelButton.addClickListener(event -> fireEvent(new CancelEvent(this)));

    saveButton.addClickShortcut(Key.ENTER);
    cancelButton.addClickShortcut(Key.ESCAPE);

    HorizontalLayout buttons = new HorizontalLayout(saveButton, deleteButton, cancelButton);
    buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    buttons.getStyle().set("margin-top", "0.75rem");

    return buttons;
  }

  private void validateAndSave() {
    try {
      binder.writeBean(user);
      if (user.getName().isEmpty() || user.getName().equals("")) {
        LOGGER.error("Username cannot be empty");
        Notification notification =
            Notification.show("Username cannot be empty", 5000, Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      } else {
        fireEvent(new SaveEvent(this, user));
      }
    } catch (ValidationException e) {
      LOGGER.error("Binder validation in the user form failed", e);
    }
  }

  /**
   * Abstract class for custom user events.
   */
  public abstract static class UserFormEvent extends ComponentEvent<UserForm> {
    private User user;

    /**
     * Holds information about the user and form.
     * 
     * <p>@param source the form that sent the event
     * 
     * <p>@param user that has been selected
     */
    protected UserFormEvent(UserForm source, User user) {
      super(source, false);
      this.user = user;
    }

    public User getUser() {
      return user;
    }
  }

  /**
   * Custom save event that can be triggered from the UI.
   */
  public static class SaveEvent extends UserFormEvent {
    SaveEvent(UserForm source, User user) {
      super(source, user);
    }
  }

  /**
   * Custom delete event that can be triggered from the UI.
   */
  public static class DeleteEvent extends UserFormEvent {
    DeleteEvent(UserForm source, User user) {
      super(source, user);
    }
  }

  /**
   * Custom cancel event that can be triggered from the UI.
   */
  public static class CancelEvent extends UserFormEvent {
    CancelEvent(UserForm source) {
      super(source, null);
    }
  }

  /**
   * Adds a listener for all the custom events.
   */
  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
      ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
