package com.alalin.todoapi.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BaseException {
  public CategoryNotFoundException(String name) {
    super("Category not found: " + name, HttpStatus.NOT_FOUND);
  }
}