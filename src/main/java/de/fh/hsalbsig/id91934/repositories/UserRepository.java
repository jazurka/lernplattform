package de.fh.hsalbsig.id91934.repositories;

import de.fh.hsalbsig.id91934.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * UserRepository serving as interface for the database tables.
 */
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Finds users based on their name.
   * 
   * <p>@param name of the user
   * 
   * <p>@return the corresponding user
   */
  User findByName(String name);

  /**
   * Finds users based on their names using the given string.
   * 
   * <p>@param searchTerm the name of the user (can be a substring of it)
   * 
   * <p>@return a list of matching users
   */
  @Query("select u from User u where lower(u.name) like lower(concat('%', :searchTerm, '%'))")
  List<User> search(@Param("searchTerm") String searchTerm);
}
