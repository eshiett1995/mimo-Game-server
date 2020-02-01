package com.gambeat.mimo.server.service.implementation;

import com.gambeat.mimo.server.model.Enum;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.repository.UserRepository;
import com.gambeat.mimo.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserServiceImplementation  implements UserService{

    @Autowired
    UserRepository userRepository;


    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public Optional<User> findExistingFacebookUser(String id, String email) {
        return userRepository.getFacebookUserByLoginTypeAndFacebookCredentialIdAndFacebookCredentialEmail(Enum.LoginType.Facebook, id,email);
    }
}
