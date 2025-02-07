package com.n1hoo.przepisy.controllers;

import com.n1hoo.przepisy.model.User;
import com.n1hoo.przepisy.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user, HttpSession session) {
        Optional<User> dbUser = userRepository.findByUsername(user.getUsername());
        if (dbUser.isPresent() && passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
            session.setAttribute("user", dbUser.get().getUsername());
            return ResponseEntity.ok("✅ Zalogowano pomyślnie!");
        }
        return ResponseEntity.status(401).body("❌ Błędne dane logowania!");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        Optional<User> existing = userRepository.findByUsername(user.getUsername());
        if (existing.isPresent()) {
            return ResponseEntity.status(400).body("❌ Użytkownik o podanej nazwie już istnieje!");
        }
        // Szyfruj hasło przed zapisem
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("✅ Zarejestrowano pomyślnie!");
}

    @GetMapping("/status")
    public ResponseEntity<String> checkLoginStatus(HttpSession session) {
        String username = (String) session.getAttribute("user");
        return username != null ? ResponseEntity.ok("✅ Zalogowany jako: " + username) : ResponseEntity.status(401).body("🚫 Nie zalogowano");
    }
}