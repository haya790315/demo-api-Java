package com.dailycodework.dreamshops.service.user;

import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UpdateUserRequest;

public interface IUserService {
  User getUserById(Long id);

  User createUser(CreateUserRequest request);

  User updateUser(Long id, UpdateUserRequest request);

  void deleteUser(Long id);

}
