package de.fh.hsalbsig.id91934.services;

import de.fh.hsalbsig.id91934.entities.User;
import de.fh.hsalbsig.id91934.repositories.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Serves as interface between UI and database.
 */
@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  /**
   * Finds user based on their name.
   * 
   * <p>@param name of the user
   * 
   * <p>@return corresponding user
   */
  public User findByName(String name) {
    return userRepository.findByName(name);
  }

  /** Finds users based on their name.
   * 
   * <p>@param filterText of the user (can be a substring)
   * 
   * <p>@return a list of matching users
   */
  public List<User> findUsers(String filterText) {
    if (filterText == null || filterText.isEmpty()) {
      return userRepository.findAll();
    } else {
      return userRepository.search(filterText);
    }
  }

  /**
   * Gets all users in the database.
   * 
   * <p>@return list of all users
   */
  public List<User> findAll() {
    return userRepository.findAll();
  }

  /**
   * Saves user in the database.
   * 
   * <p>@param user to be saved (can be changes or new entry)
   */
  public void save(User user) {
    if (user != null) {
      userRepository.save(user);
    }
  }

  /**
   * Deletes user from the database.
   * 
   * <p>@param user to be deleted
   */
  public void delete(User user) {
    if (user != null) {
      userRepository.delete(user);
    }
  }
}
