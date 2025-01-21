package de.fh.hsalbsig.id91934.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import de.fh.hsalbsig.id91934.entities.User;
import de.fh.hsalbsig.id91934.repositories.UserRepository;
import de.fh.hsalbsig.id91934.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository; // mock UserRepository

  @InjectMocks
  private UserService userService; // userService under test

  private User mockUser1;
  private User mockUser2;

  @BeforeEach
  public void setUp() {
    // set up a mock user to be used in the tests
    mockUser1 = new User();
    mockUser1.setName("test_admin");
    mockUser1.setId((long) 2);
    mockUser1.setRole("ROLE_ADMIN");
    mockUser1.setPassword("hello");
    
    mockUser2 = new User();
    mockUser2.setName("niklas");
    mockUser2.setId((long) 2);
    mockUser2.setRole("ROLE_USER");
    mockUser2.setPassword("helloooo");
  }
  
  @AfterEach
  public void tearDown() {
    userService = null;
    mockUser1 = null;
    mockUser2 = null;
  }
  
  @Test
  public void testFindByName_userFound() {
    // arrange: mock the behavior
    when(userRepository.findByName("test_admin")).thenReturn(mockUser1);
    
    // act: call method on service
    User foundUser = userService.findByName("test_admin");
    
    // assert: verify expected result
    assertNotNull(foundUser, "User should not be null");
    assertEquals("test_admin", foundUser.getName(), "User name should match");
    assertEquals((long) 2, foundUser.getId(), "User ID should match");
    
    // verify that method was called once with correct argument
    verify(userRepository, times(1)).findByName("test_admin");
  }
  
  @Test
  public void testFindByName_userNotFound() {
    // arrange: mock the behavior
    when(userRepository.findByName("")).thenReturn(null);
    
    // act: call method on service
    User foundUser = userService.findByName("");
    
    // assert: verify expected result
    assertNull(foundUser, "User should be null if empty");
    
    // verify that method was called once with correct argument
    verify(userRepository, times(1)).findByName("");
  }
  
  @Test
  public void testFindUsers_nullFilterText() {
    // arrange: mock the behavior
    List<User> users = new ArrayList<User>();
    users.add(mockUser1);
    users.add(mockUser2);
    
    when(userService.findUsers(null)).thenReturn(users);
    
    // act: call method on service
    List<User> result = userService.findUsers(null);
    
    // assert: verify expected result
    assertEquals(2, result.size());
    
    // verify the search method was never called
    verify(userRepository, never()).search(anyString());
  }
  
  @Test
  public void testFindUsers_emptyFilterText() {
    // arrange: mock the behavior
    List<User> users = new ArrayList<User>();
    users.add(mockUser1);
    users.add(mockUser2);
    
    when(userService.findUsers("")).thenReturn(users);
    
    // act: call method on service
    List<User> result = userService.findUsers("");
    
    // assert: verify expected result
    assertEquals(2, result.size());
    
    // verify the search method was never called
    verify(userRepository, never()).search(anyString());
  }
  
  @Test
  public void testFindUsers_validFilterText() {
    // arrange: mock the behavior
    List<User> users = new ArrayList<User>();
    users.add(mockUser2);
    
    when(userService.findUsers("niklas")).thenReturn(users);
    
    // act: call method on service
    List<User> result = userService.findUsers("niklas");
    
    // assert: verify expected result
    assertEquals(1, result.size());
    assertEquals(mockUser2, result.get(0));
    
    // verify the search method was never called
    verify(userRepository).search("niklas");
    verify(userRepository, never()).findAll();
  }

}
