package de.fh.hsalbsig.id91934.security;

import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.RouteAccessDeniedError;
import com.vaadin.flow.server.HttpStatusCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Overwriting the standard Vaadin error page with a custom error message.
 */
public class CustomAccessDeniedError extends RouteAccessDeniedError {
  
  private static final Logger LOGGER = LogManager.getLogger();
  
  @Override
  public int setErrorParameter(BeforeEnterEvent event,
      ErrorParameter<AccessDeniedException> parameter) {
    getElement().setText(
        "Sorry! Seems like you do not have the required permissions to access this page :(");
    getElement().getStyle().set("display", "flex").set("justify-content", "center")
        .set("align-items", "center").set("height", "100vh").set("font-size", "2.5rem")
        .set("font-weight", "bold");
    LOGGER.warn("User tried to access a page he does not have access to");
    return HttpStatusCode.UNAUTHORIZED.getCode();
  }
}
