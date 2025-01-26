package com.n1hoo.przepisy.controllers;

import com.n1hoo.przepisy.model.User;
import com.n1hoo.przepisy.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Zarządzanie użytkownikami aplikacji")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    // Tworzenie nowego użytkownika
    @PostMapping
    @Operation(summary = "Twórz użytkownika", description = "Dodaje nowego użytkownika do bazy danych")
    public User createUser(@RequestBody User user) {
        logger.info("Tworzenie nowego użytkownika: {}", user.getName());
        return userRepository.save(user);
    }

    // Pobieranie wszystkich użytkowników
    @GetMapping
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Zwraca listę wszystkich użytkowników")
    public List<User> getAllUsers() {
        logger.info("Pobieranie wszystkich użytkowników.");
        return userRepository.findAll();
    }

    // Pobieranie użytkownika po ID
    @GetMapping("/{id}")
    @Operation(summary = "Pobierz użytkownika po ID", description = "Wyszukuje użytkownika w bazie danych na podstawie ID")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        logger.info("Pobieranie użytkownika o ID: {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            logger.debug("Znaleziono użytkownika: {}", user.get().getName());
            return ResponseEntity.ok(user.get());
        } else {
            logger.warn("Nie znaleziono użytkownika o ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Aktualizacja użytkownika
    @PutMapping("/{id}")
    @Operation(summary = "Zaktualizuj użytkownika", description = "Aktualizuje dane użytkownika w bazie danych")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        logger.info("Aktualizacja użytkownika o ID: {}", id);
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            // Aktualizuj inne pola w razie potrzeby
            User updatedUser = userRepository.save(user);
            logger.debug("Zaktualizowano użytkownika: {}", user.getName());
            return ResponseEntity.ok(updatedUser);
        } else {
            logger.warn("Nie znaleziono użytkownika o ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    // Usuwanie użytkownika
    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń użytkownika", description = "Usuwa użytkownika z bazy danych na podstawie ID")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        logger.info("Usuwanie użytkownika o ID: {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            logger.debug("Usunięto użytkownika o ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            logger.warn("Nie znaleziono użytkownika o ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
