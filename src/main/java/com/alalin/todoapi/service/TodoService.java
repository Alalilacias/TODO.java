package com.alalin.todoapi.service;

import com.alalin.todoapi.entity.Category;
import com.alalin.todoapi.entity.Todo;
import com.alalin.todoapi.entity.dtos.requests.CreateTodoRequest;
import com.alalin.todoapi.entity.dtos.requests.UpdateTodoRequest;
import com.alalin.todoapi.exception.TodoNotFoundException;
import com.alalin.todoapi.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;
    private final CategoryService categoryService;

    public Todo createTodo(CreateTodoRequest request) {
        Category category = categoryService.getCategoryByName(request.category());

        Todo todo = Todo.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .category(category)
                .build();

        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, UpdateTodoRequest request) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));

        todo.setTitle(request.title());
        todo.setDescription(request.description());
        todo.setCompleted(request.completed());
        todo.setPriority(request.priority());

        if (request.category() != null) {
            Category category = categoryService.getCategoryByName(request.category());
            todo.setCategory(category);
        }

        return todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        if (!todoRepository.existsById(id)) {
            throw new TodoNotFoundException(id);
        }
        todoRepository.deleteById(id);
    }

    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    public Todo getTodoById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    public List<Todo> getCompletedTodos() {
        return todoRepository.findByCompleted(true);
    }

    public List<Todo> getTodosByCategory(String categoryName) {
        return todoRepository.findByCategory_Name(categoryName);
    }

    public List<Object[]> getTodosCountPerCategory() {
        return todoRepository.countTodosPerCategory();
    }

    public List<Object[]> getAverageTimeOpenPerCategory() {
        return todoRepository.averageTimeOpenPerCategory();
    }

    public List<Object[]> getOpenTodosByPriority() {
        return todoRepository.countOpenTodosByPriority();
    }

    public List<Todo> getCompletedToday() {
        return todoRepository.findCompletedToday();
    }

    public List<Todo> getLongestOpenTodos() {
        return todoRepository.findLongestOpenTodos();
    }

    public Object[] getTimeOpenStats() {
        return todoRepository.timeOpenStats();
    }
}