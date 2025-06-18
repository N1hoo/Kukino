package com.n1hoo.Kukino.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String role = "USER";

    // Ulubione przepisy
    private List<String> favorites = new ArrayList<>();

    // Konstruktor domyślny
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Gettery i settery
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<String> favorites) {
        this.favorites = favorites;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}