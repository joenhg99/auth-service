package com.example.auth_service.controller;

import com.example.auth_service.common.constants.ApiPathConstants;
import com.example.auth_service.common.dto.TokenResponse;
import com.example.auth_service.common.dto.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPathConstants.V1_ROUTE+ApiPathConstants.AUTH_ROUTE)
public interface AuthApi {
    @PostMapping("/register")
    ResponseEntity<TokenResponse>createUser(@RequestBody @Valid UserRequest userRequest);

    @GetMapping
    ResponseEntity<String> getUser(@RequestAttribute(name = "X-User-Id") String userId);

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody UserRequest userRequest);
}
