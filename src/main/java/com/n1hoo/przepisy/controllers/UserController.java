package com.n1hoo.przepisy.controllers;

import com.n1hoo.przepisy.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    public UserController(UserRepository userRepository) {
    }
}