package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.User;

import java.util.Optional;

public interface UserService {

    User save(User user);
    User update(User user);
    User getUserByEmail(String email);
    Optional<User> findExistingFacebookUser(String id, String email);
}
