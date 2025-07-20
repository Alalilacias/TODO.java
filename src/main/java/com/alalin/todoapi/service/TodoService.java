package com.alalin.todoapi.service;

import com.alalin.todoapi.entity.Category;
import com.alalin.todoapi.entity.Todo;
import com.alalin.todoapi.exception.CategoryNotFoundException;
import com.alalin.todoapi.exception.TodoNotFoundException;
import com.alalin.todoapi.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    public Todo createTodo(String title, String description, String categoryName, Todo.Priority priority) {
        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryName));

        Todo todo = Todo.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .category(category)
                .build();

        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, String title, String description, boolean completed, String categoryName, Todo.Priority priority) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));

        todo.setTitle(title);
        todo.setDescription(description);
        todo.setCompleted(completed);
        todo.setPriority(priority);

        if (categoryName != null) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found: " + categoryName));
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