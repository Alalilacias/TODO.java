package com.alalin.todoapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

// Alphabetical order except for Entity, which I find important to have last to be able to quick confirm its presence
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private boolean completed;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private Long timeOpen;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.completed = false;
    }

    @PreUpdate
    public void onUpdate() {
        if (this.completed && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
            this.timeOpen = Duration.between(this.createdAt, this.completedAt).getSeconds();
        }
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }
}


