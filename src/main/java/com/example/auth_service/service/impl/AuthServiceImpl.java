package com.example.auth_service.service.impl;

import com.example.auth_service.common.dto.TokenResponse;
import com.example.auth_service.common.dto.UserRequest;
import com.example.auth_service.common.entities.UserModel;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.AuthService;
import com.example.auth_service.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public TokenResponse createUser(final UserRequest userRequest) {
        return Optional.of(userRequest)
                .map(this::mapToEntity)
                .map(userRepository::save)
                .map(userCreated -> jwtService.generateToken(userCreated.getId()))
                .orElseThrow(() -> new RuntimeException("Error creating user"));
    }

    @Override
    public TokenResponse authenticate(final String email, final String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> jwtService.generateToken(user.getId()))
                .orElseThrow(() -> new RuntimeException("Email o contraseña incorrectos"));
    }


    private UserModel mapToEntity(UserRequest userRequest) {
        return UserModel.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role("USER")
                .build();
    }
}
