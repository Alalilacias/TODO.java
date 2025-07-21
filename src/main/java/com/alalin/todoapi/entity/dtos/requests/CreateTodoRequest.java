package com.alalin.todoapi.entity.dtos.requests;

import com.alalin.todoapi.entity.Todo;

public record CreateTodoRequest(
        String title,
        String description,
        String category,
        Todo.Priority priority
) {}
