package com.alalin.todoapi.repository;

import com.alalin.todoapi.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByCompleted(boolean completed);
    List<Todo> findByPriority(Todo.Priority priority);
    List<Todo> findByCategory_Name(String categoryName);
    List<Todo> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    List<Todo> findByCompletedAtIsNotNull();

    @Query(value = """
    SELECT c.name, COUNT(t.id) AS todo_count
    FROM todo t
    JOIN category c ON t.category_id = c.id
    GROUP BY c.name
""", nativeQuery = true)
    List<Object[]> countTodosPerCategory();

    @Query(value = """
    SELECT c.name, AVG(t.time_open) AS avg_time_open
    FROM todo t
    JOIN category c ON t.category_id = c.id
    WHERE t.completed = true
    GROUP BY c.name
""", nativeQuery = true)
    List<Object[]> averageTimeOpenPerCategory();

    @Query(value = """
    SELECT * FROM todo
    WHERE DATE(completed_at) = CURRENT_DATE
""", nativeQuery = true)
    List<Todo> findCompletedToday();

    @Query(value = """
    SELECT priority, COUNT(*)
    FROM todo
    WHERE completed = false
    GROUP BY priority
""", nativeQuery = true)
    List<Object[]> countOpenTodosByPriority();

    @Query(value = """
    SELECT * FROM todo
    WHERE completed = false
    ORDER BY created_at ASC
    LIMIT 5
""", nativeQuery = true)
    List<Todo> findLongestOpenTodos();

    @Query(value = """
    SELECT MIN(time_open), MAX(time_open), AVG(time_open)
    FROM todo
    WHERE completed = true
""", nativeQuery = true)
    Object[] timeOpenStats();

}
