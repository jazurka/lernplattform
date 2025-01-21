package de.fh.hsalbsig.id91934;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 * Use the @PWA annotation make the application 
 * installable on phones, tablets and some desktop
 * browsers.
 */
@SpringBootApplication
@Theme(value = "lernplatform", variant = Lumo.DARK)
public class Application implements AppShellConfigurator {
  
  /**
   * This method launches the application itself.
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
