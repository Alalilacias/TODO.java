package com.alalin.todoapi.controller;

import com.alalin.todoapi.entity.Todo;
import com.alalin.todoapi.entity.dtos.requests.CreateTodoRequest;
import com.alalin.todoapi.entity.dtos.requests.UpdateTodoRequest;
import com.alalin.todoapi.exception.ApiError;
import com.alalin.todoapi.service.TodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@Tag(name = "Todos", description = "Endpoints for managing todo tasks")
public class TodoController {

    private final TodoService todoService;

    @Operation(summary = "Create a new todo")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Todo created successfully",
                    content = @Content(schema = @Schema(implementation = Todo.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping
    public ResponseEntity<Todo> createTodo(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Todo creation request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CreateTodoRequest.class))
            )
            @RequestBody CreateTodoRequest request
    ) {
        return ResponseEntity.ok(todoService.createTodo(request));
    }


    @Operation(summary = "Update an existing todo by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Todo updated successfully",
                    content = @Content(schema = @Schema(implementation = Todo.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Todo or category not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(
            @Parameter(description = "ID of the todo to update", required = true)
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Todo update request",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdateTodoRequest.class))
            )
            @RequestBody UpdateTodoRequest request
    ) {
        return ResponseEntity.ok(todoService.updateTodo(id, request));
    }


    @GetMapping
    @Operation(summary = "Get all todos")
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @Operation(summary = "Get a todo by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todo found",
                    content = @Content(schema = @Schema(implementation = Todo.class))),
            @ApiResponse(responseCode = "404", description = "Todo not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.getTodoById(id));
    }

    @Operation(summary = "Delete a todo by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Todo deleted"),
            @ApiResponse(responseCode = "404", description = "Todo not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/completed")
    @Operation(summary = "Get all completed todos")
    public List<Todo> getCompletedTodos() {
        return todoService.getCompletedTodos();
    }

    @Operation(summary = "Get todos by category name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todos found"),
            @ApiResponse(responseCode = "404", description = "Category not found",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/by-category")
    public List<Todo> getTodosByCategory(@RequestParam String category) {
        return todoService.getTodosByCategory(category);
    }

    @Operation(summary = "Get number of todos per category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todos counted per category"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/stats/count-per-category")
    public List<Object[]> countTodosPerCategory() {
        return todoService.getTodosCountPerCategory();
    }

    @Operation(summary = "Get average time open per category (in seconds)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Average time calculated"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/stats/avg-time-per-category")
    public List<Object[]> averageTimeOpenPerCategory() {
        return todoService.getAverageTimeOpenPerCategory();
    }

    @Operation(summary = "Get count of open todos grouped by priority")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Open todos grouped by priority"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/stats/open-by-priority")
    public List<Object[]> countOpenTodosByPriority() {
        return todoService.getOpenTodosByPriority();
    }

    @Operation(summary = "Get todos completed today")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todos completed today"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/stats/completed-today")
    public List<Todo> completedToday() {
        return todoService.getCompletedToday();
    }

    @Operation(summary = "Get top 5 longest-open (incomplete) todos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Longest open todos returned"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/stats/longest-open")
    public List<Todo> longestOpen() {
        return todoService.getLongestOpenTodos();
    }

    @Operation(summary = "Get min, max, and avg time open (in seconds)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Time statistics returned"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/stats/time-open-summary")
    public Object[] timeOpenStats() {
        return todoService.getTimeOpenStats();
    }
}