package de.fh.hsalbsig.id91934.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * Landing page that serves as example.
 */
@PageTitle("Hello World")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class HelloWorldView extends HorizontalLayout {

  private TextField name;
  private Button sayHello;

  /**
   * The view creating all the UI components.
   */
  public HelloWorldView() {
    name = new TextField("Your name");
    sayHello = new Button("Say hello");
    sayHello.addClickListener(e -> {
      Notification.show("Hello " + name.getValue());
    });
    sayHello.addClickShortcut(Key.ENTER);

    setMargin(true);
    setVerticalComponentAlignment(Alignment.END, name, sayHello);

    add(name, sayHello);
  }

}
