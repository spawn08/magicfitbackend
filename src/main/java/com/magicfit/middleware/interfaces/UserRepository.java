package com.magicfit.middleware.interfaces;

import com.magicfit.middleware.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);
}
