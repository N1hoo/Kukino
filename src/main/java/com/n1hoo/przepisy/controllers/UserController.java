package com.n1hoo.przepisy.controllers;

import com.n1hoo.przepisy.model.User;
import com.n1hoo.przepisy.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
