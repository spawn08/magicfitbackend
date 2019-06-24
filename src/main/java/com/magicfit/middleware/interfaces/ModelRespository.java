package com.magicfit.middleware.interfaces;

import com.magicfit.middleware.models.ModelConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ModelRespository extends MongoRepository<ModelConfiguration, String> {

    ModelConfiguration findByEmailId(String emailId);

    ModelConfiguration findBy_id(String _id);

    ModelConfiguration findByToken(String token);
}
