package com.dailycodework.dreamshops.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dailycodework.dreamshops.exceptions.AlreadyExistsException;
import com.dailycodework.dreamshops.exceptions.ResourceNotFoundException;
import com.dailycodework.dreamshops.exceptions.UpdateException;
import com.dailycodework.dreamshops.model.User;
import com.dailycodework.dreamshops.request.CreateUserRequest;
import com.dailycodework.dreamshops.request.UpdateUserRequest;
import com.dailycodework.dreamshops.response.ApiResponse;
import com.dailycodework.dreamshops.service.user.IUserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
  private final IUserService userService;

  @GetMapping("/{userId}/user")
  public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
    try {
      User user = userService.getUserById(userId);
      return ResponseEntity.ok(new ApiResponse("Success 🥳", user.convertToDto()));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User Not Found 😵‍💫", null));
    }
  }

  @PostMapping("/create")
  public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
    try {
      User user = userService.createUser(request);
      return ResponseEntity.ok(new ApiResponse("User Created 🥳", user.convertToDto()));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @PutMapping("/{userId}/update")
  public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest request) {
    try {
      User user = userService.updateUser(userId, request);
      return ResponseEntity.ok(new ApiResponse("User Updated 🥳", user.convertToDto()));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User Not Found 😵‍💫", null));
    } catch (UpdateException e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

  @DeleteMapping("/{userId}/delete")
  public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
    try {
      userService.deleteUser(userId);
      return ResponseEntity.ok(new ApiResponse("User Deleted 🥳", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("User Not Found 😵‍💫", null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
    }
  }

}
