package com.n1hoo.przepisy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recipes") // WAŻNE: MongoDB musi mieć poprawną nazwę kolekcji!
public class Recipe {
    @Id
    private String id;
    private String title;
    private List<String> ingredients;
    private String instructions;
    private String author;
    private int popularity;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;

    // 🔹 Konstruktor domyślny wymagany przez Spring Boot
    public Recipe() {
        this.createdAt = LocalDateTime.now();
    }

    // 🔹 Konstruktor inicjalizujący wszystkie pola
    public Recipe(String title, List<String> ingredients, String instructions, String author) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.author = author;
        this.popularity = 0;
        this.createdAt = LocalDateTime.now();
        this.publishedAt = null;
    }

    // 🔹 Gettery i settery
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public List<String> getIngredients() { return ingredients; }
    public void setIngredients(List<String> ingredients) { this.ingredients = ingredients; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getPopularity() { return popularity; }
    public void setPopularity(int popularity) { this.popularity = popularity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getPublishedAt() { return publishedAt; }
    public void setPublishedAt(LocalDateTime publishedAt) { this.publishedAt = publishedAt; }
}
