package com.dailycodework.dreamshops.exceptions;

public class AlreadyExistsException extends RuntimeException {
  public AlreadyExistsException(String item) {
    super(item + "already exists");
  }
}
