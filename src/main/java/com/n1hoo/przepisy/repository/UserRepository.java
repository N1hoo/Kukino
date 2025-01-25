package com.n1hoo.przepisy.repository;

import com.n1hoo.przepisy.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    // Dodatkowe metody zapytań, jeśli potrzebne
}
