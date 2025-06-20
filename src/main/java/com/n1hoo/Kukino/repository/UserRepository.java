package com.n1hoo.Kukino.repository;

import com.n1hoo.Kukino.model.User;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}