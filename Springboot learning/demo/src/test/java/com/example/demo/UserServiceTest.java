package com.example.demo;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  public UserServiceTest () {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testGetAllUsers() {

    User john = new User("John Doe", "john@test.com");
    User jane = new User("Jane Doe", "jane@test.com");

    when(userRepository.findAll()).thenReturn(Arrays.asList(john, jane));


    //Record result
    List<User> result = userService.getAllUsers();

    //Verify results.
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getName()).isEqualTo("John Doe");
  }

  @Test
  void testGetUserById_Found(){
    User user = new User("John Doe", "john@test.com");

    when(userRepository.findById((long) 1)).thenReturn(Optional.of(user));

    
    User result = userService.getUserById((long)1);

    assertThat(result.getEmail()).isEqualTo(user.getEmail());
    assertThat(result.getName()).isEqualTo(user.getName());
  }

  @Test
  void testGetUserById_NotFound(){
    when(userRepository.findById((long) 1)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> userService.getUserById((long) 1))
      .isInstanceOf(RuntimeException.class)
      .hasMessage("User not found");
  }

  @Test
  void testCreateUser () {
    UserDto dto = new UserDto("John Doe", "john@test.com");
    User savedUser = new User("John Doe", "john@test.com");

    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    User result = userService.createUser(dto);

    assertThat(result.getName()).isEqualTo("John Doe");
    assertThat(result.getEmail()).isEqualTo("john@test.com");
  }

  @Test
  void testDeleteUser_Success() {
    userRepository.deleteById((long) 1);

    //delete was called 1 time
    verify(userRepository, times(1)).deleteById((long) 1);
  }

  @Test
  void testDeleteUser_Fail() {
    doThrow(new RuntimeException("User Not Found"))
    .when(userRepository)
    .deleteById((long) 1);

    //Call delete user through service
    userService.deleteUser((long) 1);

    //Verify deleteById is called
    verify(userRepository, times(1)).deleteById((long) 99);
  }
}
