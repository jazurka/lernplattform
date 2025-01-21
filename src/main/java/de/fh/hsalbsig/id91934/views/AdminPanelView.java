package de.fh.hsalbsig.id91934.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.fh.hsalbsig.id91934.entities.User;
import de.fh.hsalbsig.id91934.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Admin Panel used for viewing and editing users.
 */
@SuppressWarnings("serial")
@PageTitle("Admin Panel")
@Route(value = "adminPanel", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
public class AdminPanelView extends VerticalLayout {

  private TextField filterText = new TextField();
  private Grid<User> userGrid = new Grid<>(User.class);
  private UserForm userForm;

  private transient UserService userService;

  private static final Logger LOGGER = LogManager.getLogger();
  
  /**
   * The view creating all the UI components.
   * 
   * <p>@param userService gets autowired for database operations
   */
  @Autowired
  public AdminPanelView(UserService userService) {
    setSizeFull();

    this.userService = userService;

    configureSearch();
    configureGrid();
    configureForm();

    updateGrid();
    closeEditor();

    add(filterText, getContent());
  }

  private void closeEditor() {
    userForm.setUser(null);
    userForm.setVisible(false);
  }

  private void updateGrid() {
    userGrid.setItems(userService.findUsers(filterText.getValue()));
    LOGGER.info("Got all users from the database");
  }

  private Component getContent() {
    HorizontalLayout content = new HorizontalLayout(userGrid, userForm);

    content.setFlexGrow(2, userGrid);
    content.setFlexGrow(1, userForm);
    content.setSizeFull();

    return content;
  }

  private void configureForm() {
    userForm = new UserForm();
    userForm.setWidth("25em");

    userForm.addListener(UserForm.SaveEvent.class, e -> saveUser(e));
    userForm.addListener(UserForm.DeleteEvent.class, e -> deleteUser(e));
    userForm.addListener(UserForm.CancelEvent.class, e -> closeEditor());
  }

  private void deleteUser(UserForm.DeleteEvent deleteEvent) {
    userService.delete(deleteEvent.getUser());
    LOGGER.info("Deleted user '{}' from the database", deleteEvent.getUser().getName());
    updateGrid();
    closeEditor();
  }

  private void saveUser(UserForm.SaveEvent saveEvent) {
    userService.save(saveEvent.getUser());
    LOGGER.info("Saved user '{}' in the database", saveEvent.getUser().getName());
    updateGrid();
    closeEditor();
  }

  private void configureSearch() {
    filterText.setClearButtonVisible(true);
    filterText.setPlaceholder("Filter by name...");
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(e -> updateGrid());
  }

  private void configureGrid() {
    userGrid.setSizeFull();
    userGrid.removeAllColumns();
    userGrid.addColumn(User::getName).setHeader("Name");
    // userGrid.addColumn(user -> user.getRole().substring(5).toLowerCase()).setHeader("Role");
    userGrid.addColumn(User::getRole).setHeader("Role");
    userGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    userGrid.asSingleSelect().addValueChangeListener(event -> editUser(event.getValue()));
  }

  private void editUser(User user) {
    if (user == null) {
      closeEditor();
    } else {
      userForm.setUser(user);
      userForm.setVisible(true);
    }
  }

}
