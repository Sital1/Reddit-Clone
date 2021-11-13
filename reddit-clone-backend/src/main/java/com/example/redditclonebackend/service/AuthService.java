package com.example.redditclonebackend.service;

import com.example.redditclonebackend.dto.RegisterRequestDTO;

public interface AuthService {

    void signup(RegisterRequestDTO registerRequestDTO);

    void verifyAccount(String token);
}
