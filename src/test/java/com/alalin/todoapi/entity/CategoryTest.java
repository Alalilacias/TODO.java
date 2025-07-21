package com.alalin.todoapi.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testGettersAndSetters() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Work");

        assertEquals(1L, category.getId());
        assertEquals("Work", category.getName());
    }

    @Test
    void testAllArgsConstructor() {
        Category category = new Category(2L, "Personal");

        assertEquals(2L, category.getId());
        assertEquals("Personal", category.getName());
    }

    @Test
    void testBuilder() {
        Category category = Category.builder()
                .id(3L)
                .name("Health")
                .build();

        assertEquals(3L, category.getId());
        assertEquals("Health", category.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        Category cat1 = new Category(1L, "Work");
        Category cat2 = new Category(1L, "Work");

        assertEquals(cat1, cat2);
        assertEquals(cat1.hashCode(), cat2.hashCode());
    }

    @Test
    void testToString() {
        Category category = new Category(4L, "Study");

        String expected = "Category(id=4, name=Study)";
        assertEquals(expected, category.toString());
    }
}