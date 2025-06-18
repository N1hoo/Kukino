package com.n1hoo.Kukino.controllers;

import com.n1hoo.Kukino.model.User;
import com.n1hoo.Kukino.repository.RecipeRepository;
import com.n1hoo.Kukino.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Funkcje administracyjne systemu")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public AdminController(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    @Operation(summary = "Pobierz listę użytkowników", description = "Zwraca listę wszystkich użytkowników (tylko dla administratora)")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        logger.info("Admin żąda listy użytkowników");
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Usuń przepis", description = "Usuwa przepis z bazy danych (tylko dla administratora)")
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable String id) {
        logger.info("Admin usuwa przepis o ID: {}", id);
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Utwórz konto administratora", description = "Tworzy konto administratora. Upewnij się, że dane wejściowe są poprawne.")
    @PostMapping("/createAdmin")
    public ResponseEntity<User> createAdmin(@RequestBody User user) {
        logger.info("Tworzenie nowego administratora: {}", user.getUsername());
        user.setRole("ADMIN");
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }
}