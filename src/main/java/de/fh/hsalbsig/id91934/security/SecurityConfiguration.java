package de.fh.hsalbsig.id91934.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import de.fh.hsalbsig.id91934.entities.User;
import de.fh.hsalbsig.id91934.services.UserService;
import de.fh.hsalbsig.id91934.views.LoginView;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


/**
 * Custom security class managing login process and checking if users are logged in.
 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends VaadinWebSecurity {
  
  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        auth -> auth.requestMatchers(new AntPathRequestMatcher("/public/**")).permitAll());

    super.configure(http);
    setLoginView(http, LoginView.class);
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    super.configure(web);
  }

  /**
   * Creates a BCrypt encoder for password hashing.
   * 
   * <p>@return the encoder object for password hashing
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * The service that fetches the user and gives them rights.
   * 
   * <p>@param userService that handles user data from the database
   * 
   * <p>@return userDetailsService to give users access
   */
  @Bean
  public UserDetailsService userDetailsService(UserService userService) {
    return name -> {
      User user = userService.findByName(name);
      if (user == null) {
        LOGGER.error("User: '{}' not found in the database", name);
        throw new UsernameNotFoundException("user not found");
      }
      return switch (user.getRole()) {
        case "ROLE_USER" -> new org.springframework.security.core.userdetails.User(user.getName(),
            user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
        case "ROLE_ADMIN" -> new org.springframework.security.core.userdetails.User(user.getName(),
            user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        case "ROLE_PROF" -> new org.springframework.security.core.userdetails.User(user.getName(),
            user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_PROF")));
        default -> {
          LOGGER.error("User: '{}' not found in the database", name);
          throw new UsernameNotFoundException("user not found");
        }
      };
    };
  }
}
