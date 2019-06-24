package com.spawnai.middleware.interfaces;

import com.spawnai.middleware.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
}
