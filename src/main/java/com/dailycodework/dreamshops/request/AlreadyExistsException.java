package com.dailycodework.dreamshops.request;

public class AlreadyExistsException extends RuntimeException {
  public AlreadyExistsException(String item) {
    super(item + "already exists");
  }
}
