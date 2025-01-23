package com.dailycodework.dreamshops.exceptions;

public class LowStockException extends RuntimeException {

  public LowStockException(String item) {
    super(item + "stock not enough");
  }

}