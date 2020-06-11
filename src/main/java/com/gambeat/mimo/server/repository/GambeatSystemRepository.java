package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.GambeatSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GambeatSystemRepository extends MongoRepository<GambeatSystem,String> {

}
