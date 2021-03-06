package com.gambeat.mimo.server.repository;

import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String>{

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserById(String id);

    Optional<User> getFacebookUserByLoginTypeAndFacebookCredentialIdAndFacebookCredentialEmail(Enum.LoginType loginType, String id, String email);

}
