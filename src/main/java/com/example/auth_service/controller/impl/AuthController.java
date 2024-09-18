package com.example.auth_service.controller.impl;

import com.example.auth_service.common.dto.TokenResponse;
import com.example.auth_service.common.dto.UserRequest;
import com.example.auth_service.controller.AuthApi;
import com.example.auth_service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<TokenResponse> createUser(final UserRequest userRequest) {
        return ResponseEntity.ok(authService.createUser(userRequest));
    }

    @Override
    public ResponseEntity<String> getUser(final String userId) {
        return ResponseEntity.ok(userId);
    }

    @Override
    public ResponseEntity<TokenResponse> login(final UserRequest userRequest) {
        return ResponseEntity.ok(this.authService.authenticate(userRequest.getEmail(), userRequest.getPassword()));
    }

}
