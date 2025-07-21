package com.alalin.todoapi.entity.dtos.requests;

import com.alalin.todoapi.entity.Todo;

public record UpdateTodoRequest(
        String title,
        String description,
        boolean completed,
        String category,
        Todo.Priority priority
) {}