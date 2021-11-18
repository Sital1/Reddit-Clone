package com.example.redditclonebackend.service;

import com.example.redditclonebackend.dto.AuthenticationResponseDto;
import com.example.redditclonebackend.dto.LoginRequestDto;
import com.example.redditclonebackend.dto.RegisterRequestDTO;
import com.example.redditclonebackend.model.User;

public interface AuthService {

    void signup(RegisterRequestDTO registerRequestDTO);

    void verifyAccount(String token);

    AuthenticationResponseDto login(LoginRequestDto loginRequestDto);

    User getCurrentUser();
}
