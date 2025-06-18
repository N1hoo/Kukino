package com.n1hoo.Kukino.controllers;

import com.n1hoo.Kukino.model.Recipe;
import com.n1hoo.Kukino.model.User;
import com.n1hoo.Kukino.repository.RecipeRepository;
import com.n1hoo.Kukino.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final RecipeRepository recipeRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserController(UserRepository userRepository, RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }
        // 1. Dodanie przepisu do ulubionych
    @PostMapping("/favorites/{recipeId}")
    public ResponseEntity<String> addFavorite(@PathVariable String recipeId, HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }

        // Sprawdź, czy przepis istnieje
        if (!recipeRepository.existsById(recipeId)) {
            return ResponseEntity.status(404).body("❌ Nie ma takiego przepisu!");
        }

        // Wczytaj użytkownika i dodaj ID przepisu do listy
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("❌ Nie znaleziono użytkownika");
        }

        if (!user.getFavorites().contains(recipeId)) {
            user.getFavorites().add(recipeId);
            userRepository.save(user);
        }
        return ResponseEntity.ok("✅ Dodano do ulubionych!");
    }

    // 2. Usunięcie przepisu z ulubionych
    @DeleteMapping("/favorites/{recipeId}")
    public ResponseEntity<String> removeFavorite(@PathVariable String recipeId, HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("❌ Nie znaleziono użytkownika");
        }

        if (user.getFavorites().contains(recipeId)) {
            user.getFavorites().remove(recipeId);
            userRepository.save(user);
            return ResponseEntity.ok("✅ Usunięto z ulubionych!");
        } else {
            return ResponseEntity.status(404).body("❌ Ten przepis nie jest w ulubionych!");
        }
    }

    // 3. Pobranie listy ulubionych (wraz z danymi przepisów)
    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites(HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("❌ Nie znaleziono użytkownika");
        }

        // Pobieramy przepisy na podstawie listy ID w `user.getFavorites()`
        List<Recipe> favoriteRecipes = recipeRepository.findAllById(user.getFavorites());
        return ResponseEntity.ok(favoriteRecipes);
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody Map<String, String> request, HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }
        String newPassword = request.get("password");
        if (newPassword == null || newPassword.isBlank()) {
            return ResponseEntity.badRequest().body("⚠️ Podaj nowe hasło!");
        }
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Użytkownik nie znaleziony!");
        }
        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok("✅ Hasło zmienione!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Użytkownik nie znaleziony!");
        }
        userRepository.delete(userOpt.get());
        session.invalidate();
        return ResponseEntity.ok("✅ Konto usunięte!");
    }
}