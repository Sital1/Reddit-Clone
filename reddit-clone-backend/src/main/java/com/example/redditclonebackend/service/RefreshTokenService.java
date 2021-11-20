package com.example.redditclonebackend.service;

import com.example.redditclonebackend.model.RefreshToken;

public interface RefreshTokenService {

    RefreshToken generateRefreshToken();
    void validateRefreshToken(String token);
    void deleteRefreshToken(String token);
}
