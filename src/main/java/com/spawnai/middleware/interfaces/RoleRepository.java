package com.spawnai.middleware.interfaces;

import com.spawnai.middleware.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}
