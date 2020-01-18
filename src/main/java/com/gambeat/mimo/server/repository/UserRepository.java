package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{

}