package com.gambeat.mimo.server.service;

import com.gambeat.mimo.server.model.User;

public interface UserService {

    User save(User user);
    User update(User user);
    User getUserByEmail(String email);
}
