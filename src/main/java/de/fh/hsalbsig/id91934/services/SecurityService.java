package de.fh.hsalbsig.id91934.services;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

/**
 * Security Service to retrieve authentication and handle logout process.
 */
@Component
public class SecurityService {

  private static final String LOGOUT_SUCCESS_URL = "/";

  /**
   * Retrieves information about the current logged in user.
   * 
   * <p>@return userDetails of the current user.
   */
  public UserDetails getAuthenticatedUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    Object principal = context.getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return (UserDetails) context.getAuthentication().getPrincipal();
    }
    return null;
  }

  /**
   * Handles the logout process.
   */
  public void logout() {
    UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
  }
}
