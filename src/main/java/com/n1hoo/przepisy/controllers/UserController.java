package com.n1hoo.przepisy.controllers;

import com.n1hoo.przepisy.model.User;
import com.n1hoo.przepisy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> changePassword(@RequestBody User user, HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }

        Optional<User> dbUser = userRepository.findByUsername(username);
        if (dbUser.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Użytkownik nie istnieje!");
        }

        dbUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(dbUser.get());
        return ResponseEntity.ok("✅ Hasło zostało zmienione!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAccount(HttpSession session) {
        String username = (String) session.getAttribute("user");
        if (username == null) {
            return ResponseEntity.status(401).body("🚫 Nie jesteś zalogowany!");
        }

        Optional<User> dbUser = userRepository.findByUsername(username);
        if (dbUser.isEmpty()) {
            return ResponseEntity.status(404).body("❌ Użytkownik nie istnieje!");
        }

        userRepository.delete(dbUser.get());
        session.invalidate();
        return ResponseEntity.ok("✅ Konto zostało usunięte!");
    }
}