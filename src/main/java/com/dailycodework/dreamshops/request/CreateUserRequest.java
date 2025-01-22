package com.dailycodework.dreamshops.request;

import lombok.Data;

@Data
public class CreateUserRequest {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private String password;

  public CreateUserRequest(String firstName, String lastName, String email, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.password = password;
  }

}
