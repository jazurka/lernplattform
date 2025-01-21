package de.fh.hsalbsig.id91934.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * This class holds the user information from the database.
 */
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String password;

  private String role;

  /**
   * Empty constructor.
   * Can be used when creating a new object.
   */
  public User() {

  }

  /**
   * This method creates a new user filling all of its properties. 
   * Used by the database when fetching data.
   * 
   * <p>@param name of the user
   * 
   * <p>@param password of the user / using BCrypt encoder for hashing
   * 
   * <p>@param role of the user / either ROLE_ADMIN, ROLE_USER or ROLE_PROF
   */
  public User(String name, String password, String role) {
    String bcryptPattern = "^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$";
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    if (password.length() != 60) {
      this.password = passwordEncoder.encode(password);
    } else {
      Pattern pattern = Pattern.compile(bcryptPattern);
      Matcher matcher = pattern.matcher(password);
      if (matcher.matches()) {
        this.password = password;
      } else {
        this.password = passwordEncoder.encode(password);
      }
    }

    this.name = name;
    this.role = role;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return this.password;
  }

  /**
   * This method sets the password of the user using the BCrypt encoder.
   * 
   * <p>@param password of the user
   */
  public void setPassword(String password) {
    String bcryptPattern = "^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$";
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    if (password == null || password.length() != 60) {
      this.password = passwordEncoder.encode(password);
    } else {
      Pattern pattern = Pattern.compile(bcryptPattern);
      Matcher matcher = pattern.matcher(password);
      if (matcher.matches()) {
        this.password = password;
      } else {
        this.password = passwordEncoder.encode(password);
      }
    }
  }

  public String getRole() {
    return this.role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
