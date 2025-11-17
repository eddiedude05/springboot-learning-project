package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.exception.UserNotFoundException;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  //GET ALL
  public List<User> getAllUsers(){
    return userRepository.findAll();
  }

  //GET
  public User getUserById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  //POST
  public User createUser(UserDto userDto) {
    User user = new User(userDto.getName(), userDto.getEmail());
    return userRepository.save(user);
  }

  //PUT
  public User updateUser(Long id, UserDto userDto){
    return userRepository.findById(id).map(user -> {
      user.setName(userDto.getName());
      user.setEmail(userDto.getEmail());
      return userRepository.save(user);
    }).orElseThrow(() -> new UserNotFoundException(id));
  }

  //DELETE
  public void deleteUser(Long id) {
    
    if(!userRepository.existsById(id)) {
      throw new UserNotFoundException(id);
    }
    userRepository.deleteById(id);
  }

}
