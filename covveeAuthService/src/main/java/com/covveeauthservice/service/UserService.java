package com.covveeauthservice.service;

import com.covveeauthservice.mapper.UserMapper;
import com.covveeauthservice.repository.UserRepository;

public class UserService {
    private UserMapper usermapper;
    private UserRepository userRepository;

    public UserService(UserMapper usermapper, UserRepository userRepository) {
        this.usermapper = usermapper;
        this.userRepository = userRepository;
    }

}
