package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.dto.RegisterRequestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/api")
public class AuthController {

    @PostMapping("/signup")
    public void signup(RegisterRequestDTO registerRequest){

    }
}
