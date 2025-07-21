package com.alalin.todoapi.service;

import com.alalin.todoapi.entity.Category;
import com.alalin.todoapi.entity.Todo;
import com.alalin.todoapi.entity.Todo.Priority;
import com.alalin.todoapi.entity.dtos.requests.CreateTodoRequest;
import com.alalin.todoapi.entity.dtos.requests.UpdateTodoRequest;
import com.alalin.todoapi.exception.TodoNotFoundException;
import com.alalin.todoapi.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock private TodoRepository todoRepository;
    @Mock private CategoryService categoryService;
    @InjectMocks private TodoService todoService;

    private CreateTodoRequest createReq;
    private UpdateTodoRequest updateReq;
    private Category category;
    private Todo todo;

    @BeforeEach
    void setup() {
        category = new Category(1L, "Work");
        createReq = new CreateTodoRequest("Title", "Desc", "Work", Priority.HIGH);
        updateReq = new UpdateTodoRequest("New Title", "New Desc", true, "Work", Priority.MEDIUM);
        todo = Todo.builder().id(1L).title("Old").build();
    }

    @Test
    void createTodo_createsAndSaves() {
        when(categoryService.getCategoryByName("Work")).thenReturn(category);
        when(todoRepository.save(any(Todo.class))).thenAnswer(inv -> inv.getArgument(0));

        Todo result = todoService.createTodo(createReq);

        assertEquals("Title", result.getTitle());
        assertEquals(category, result.getCategory());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void updateTodo_updatesAndSaves() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(categoryService.getCategoryByName("Work")).thenReturn(category);
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo updated = todoService.updateTodo(1L, updateReq);

        assertEquals("New Title", updated.getTitle());
        assertTrue(updated.isCompleted());
        assertEquals(Priority.MEDIUM, updated.getPriority());
    }

    @Test
    void updateTodo_withNullCategory_skipsCategoryUpdate() {
        UpdateTodoRequest noCat = new UpdateTodoRequest("New", "Desc", false, null, Priority.LOW);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoRepository.save(todo)).thenReturn(todo);

        Todo updated = todoService.updateTodo(1L, noCat);

        assertEquals("New", updated.getTitle());
        verify(categoryService, never()).getCategoryByName(any());
    }

    @Test
    void updateTodo_notFound_throws() {
        when(todoRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(TodoNotFoundException.class, () -> todoService.updateTodo(2L, updateReq));
    }

    @Test
    void deleteTodo_exists_deletes() {
        when(todoRepository.existsById(1L)).thenReturn(true);

        todoService.deleteTodo(1L);

        verify(todoRepository).deleteById(1L);
    }

    @Test
    void deleteTodo_notFound_throws() {
        when(todoRepository.existsById(2L)).thenReturn(false);
        assertThrows(TodoNotFoundException.class, () -> todoService.deleteTodo(2L));
    }

    @Test
    void getAllTodos_delegatesToRepo() {
        List<Todo> list = List.of(todo);
        when(todoRepository.findAll()).thenReturn(list);

        assertEquals(list, todoService.getAllTodos());
    }

    @Test
    void getTodoById_found_returns() {
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        assertEquals(todo, todoService.getTodoById(1L));
    }

    @Test
    void getTodoById_notFound_throws() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(TodoNotFoundException.class, () -> todoService.getTodoById(1L));
    }

    @Test
    void getCompletedTodos_delegatesToRepo() {
        when(todoRepository.findByCompleted(true)).thenReturn(List.of(todo));
        assertEquals(1, todoService.getCompletedTodos().size());
    }

    @Test
    void getTodosByCategory_delegatesToRepo() {
        when(todoRepository.findByCategory_Name("Work")).thenReturn(List.of(todo));
        assertEquals(1, todoService.getTodosByCategory("Work").size());
    }

    @Test
    void getTodosCountPerCategory_delegatesToRepo() {
        List<Object[]> data = Collections.singletonList(new Object[]{"Work", 5L});
        when(todoRepository.countTodosPerCategory()).thenReturn(data);
        assertEquals(data, todoService.getTodosCountPerCategory());
    }

    @Test
    void getAverageTimeOpenPerCategory_delegatesToRepo() {
        List<Object[]> data = Collections.singletonList(new Object[]{"Work", 3600.0});
        when(todoRepository.averageTimeOpenPerCategory()).thenReturn(data);
        assertEquals(data, todoService.getAverageTimeOpenPerCategory());
    }

    @Test
    void getOpenTodosByPriority_delegatesToRepo() {
        List<Object[]> data = Collections.singletonList(new Object[]{"HIGH", 3L});
        when(todoRepository.countOpenTodosByPriority()).thenReturn(data);
        assertEquals(data, todoService.getOpenTodosByPriority());
    }

    @Test
    void getCompletedToday_delegatesToRepo() {
        when(todoRepository.findCompletedToday()).thenReturn(List.of(todo));
        assertEquals(1, todoService.getCompletedToday().size());
    }

    @Test
    void getLongestOpenTodos_delegatesToRepo() {
        when(todoRepository.findLongestOpenTodos()).thenReturn(List.of(todo));
        assertEquals(1, todoService.getLongestOpenTodos().size());
    }

    @Test
    void getTimeOpenStats_delegatesToRepo() {
        Object[] stats = new Object[]{100L, 5000L, 50L};
        when(todoRepository.timeOpenStats()).thenReturn(stats);
        assertEquals(stats, todoService.getTimeOpenStats());
    }
}