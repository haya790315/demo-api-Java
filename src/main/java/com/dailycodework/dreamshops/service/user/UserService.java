package com.dailycodework.dreamshops.service.user;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.repository.UserRepository;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UpdateUserRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

  private final UserRepository userRepository;

  @Override
  public User getUserById(Long id) {
    return findUserByIdOrThrow(id);
  }

  @Override
  public User createUser(CreateUserRequest request) {
    return Optional.of(request).filter(user -> !userRepository.existsByEmail(request.getEmail()))
        .map(r -> new User(r.getFirstName(), r.getLastName(), r.getEmail(), r.getPassword()))
        .map(userRepository::save)
        .orElseThrow(() -> new AlreadyExistsException("Email"));
  }

  @Override
  public User updateUser(Long id, UpdateUserRequest request) {
    User user = findUserByIdOrThrow(id);
    user.updateUser(request);
    return userRepository.save(user);
  }

  @Override
  public void deleteUser(Long id) {
    User user = findUserByIdOrThrow(id);
    userRepository.delete(user);
  }

  private User findUserByIdOrThrow(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User Not Exit"));
  }

}
