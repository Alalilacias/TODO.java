package com.alalin.todoapi.exception;

import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistsException extends BaseException {
    public CategoryAlreadyExistsException(String name) {
        super("Category already exists: " + name, HttpStatus.BAD_REQUEST);
    }
}