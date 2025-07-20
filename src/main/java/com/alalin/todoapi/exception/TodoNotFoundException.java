package com.alalin.todoapi.exception;

import org.springframework.http.HttpStatus;

public class TodoNotFoundException extends BaseException {
    public TodoNotFoundException(Long id) {
        super("Todo not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}