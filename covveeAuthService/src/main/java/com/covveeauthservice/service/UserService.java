package com.covveeauthservice.service;

import com.covveeauthservice.dto.CreateUserDto;
import com.covveeauthservice.entities.User;
import com.covveeauthservice.mapper.UserMapper;
import com.covveeauthservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserService {
    private UserMapper usermapper;
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public UserService(UserMapper usermapper, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder ) {
        this.usermapper = usermapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CreateUserDto createUser(CreateUserDto dto){
        userRepository.findByEmail(dto.getEmail()).orElseThrow(()->new RuntimeException("user with this email already exist"));
        User user = usermapper.toEntity(dto);
        userRepository.save(user);
        return dto;
    }
    public User logIn(){
        return null;
    }
}
