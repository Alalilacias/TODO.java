package com.alalin.todoapi.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TodoTest {

    @Test
    void testGettersAndSetters() {
        Todo todo = new Todo();
        Category category = new Category(1L, "Work");

        todo.setId(1L);
        todo.setTitle("Write tests");
        todo.setDescription("Write unit tests for the Todo class");
        todo.setCompleted(true);
        todo.setCreatedAt(LocalDateTime.of(2025, 7, 20, 12, 0));
        todo.setCompletedAt(LocalDateTime.of(2025, 7, 21, 12, 0));
        todo.setTimeOpen(86400L);
        todo.setPriority(Todo.Priority.HIGH);
        todo.setCategory(category);

        assertEquals(1L, todo.getId());
        assertEquals("Write tests", todo.getTitle());
        assertEquals("Write unit tests for the Todo class", todo.getDescription());
        assertTrue(todo.isCompleted());
        assertEquals(LocalDateTime.of(2025, 7, 20, 12, 0), todo.getCreatedAt());
        assertEquals(LocalDateTime.of(2025, 7, 21, 12, 0), todo.getCompletedAt());
        assertEquals(86400L, todo.getTimeOpen());
        assertEquals(Todo.Priority.HIGH, todo.getPriority());
        assertEquals(category, todo.getCategory());
    }

    @Test
    void testBuilder() {
        Category category = new Category(2L, "Personal");

        Todo todo = Todo.builder()
                .id(2L)
                .title("Read book")
                .description("Read effective Java")
                .completed(false)
                .createdAt(LocalDateTime.of(2025, 7, 20, 15, 30))
                .completedAt(null)
                .timeOpen(null)
                .priority(Todo.Priority.MEDIUM)
                .category(category)
                .build();

        assertEquals(2L, todo.getId());
        assertEquals("Read book", todo.getTitle());
        assertEquals("Read effective Java", todo.getDescription());
        assertFalse(todo.isCompleted());
        assertEquals(LocalDateTime.of(2025, 7, 20, 15, 30), todo.getCreatedAt());
        assertNull(todo.getCompletedAt());
        assertNull(todo.getTimeOpen());
        assertEquals(Todo.Priority.MEDIUM, todo.getPriority());
        assertEquals(category, todo.getCategory());
    }

    @Test
    void testOnCreate() {
        Todo todo = new Todo();
        todo.onCreate();

        assertNotNull(todo.getCreatedAt());
        assertFalse(todo.isCompleted());
    }

    @Test
    void testOnUpdateWhenCompletedAndNoCompletedAt() {
        Todo todo = new Todo();
        todo.setCreatedAt(LocalDateTime.now().minusHours(2));
        todo.setCompleted(true);

        todo.onUpdate();

        assertNotNull(todo.getCompletedAt());
        assertNotNull(todo.getTimeOpen());
        assertTrue(todo.getTimeOpen() > 0);
    }

    @Test
    void testOnUpdateWhenAlreadyHasCompletedAt() {
        Todo todo = new Todo();
        todo.setCreatedAt(LocalDateTime.now().minusHours(2));
        todo.setCompleted(true);
        todo.setCompletedAt(LocalDateTime.now().minusHours(1));

        todo.onUpdate();

        // Should not overwrite
        LocalDateTime existing = todo.getCompletedAt();
        todo.onUpdate();
        assertEquals(existing, todo.getCompletedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        Todo t1 = Todo.builder().id(1L).title("A").build();
        Todo t2 = Todo.builder().id(1L).title("A").build();

        assertEquals(t1, t2);
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @Test
    void testToString() {
        Todo todo = Todo.builder().id(99L).title("T").build();
        assertTrue(todo.toString().contains("Todo"));
        assertTrue(todo.toString().contains("99"));
        assertTrue(todo.toString().contains("T"));
    }

    @Test
    void testPriorityEnumValues() {
        assertEquals("LOW", Todo.Priority.LOW.name());
        assertEquals("MEDIUM", Todo.Priority.MEDIUM.name());
        assertEquals("HIGH", Todo.Priority.HIGH.name());
        assertEquals("CRITICAL", Todo.Priority.CRITICAL.name());
    }
}