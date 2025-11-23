package com.covveeauthservice.service;

import com.covveeauthservice.dto.CreateUserDto;
import com.covveeauthservice.dto.LoginDTO;
import com.covveeauthservice.entities.User;
import com.covveeauthservice.mapper.UserMapper;
import com.covveeauthservice.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserMapper usermapper;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserMapper usermapper, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder ) {
        this.usermapper = usermapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CreateUserDto createUser(CreateUserDto dto){
        userRepository.findByEmail(dto.getEmail()).orElseThrow(()->new RuntimeException("user with this email already exist"));
        String hashedPassword = passwordEncoder.encode(dto.getPassword());
        dto.setPassword(hashedPassword);
        User user = usermapper.toEntity(dto);
        userRepository.save(user);
        return dto;
    }
    public User logIn(LoginDTO dto){
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(()->new RuntimeException("no user found with this email"));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password is not correct");
        }
        return user;
    }
}
