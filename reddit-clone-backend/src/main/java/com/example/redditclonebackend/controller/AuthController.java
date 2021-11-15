package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.dto.AuthenticationResponseDto;
import com.example.redditclonebackend.dto.LoginRequestDto;
import com.example.redditclonebackend.dto.RegisterRequestDTO;
import com.example.redditclonebackend.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;


    /**
     * Endpoint to handle user signup
     * @param registerRequest User Data
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequestDTO registerRequest){
        System.out.println(registerRequest);
        authService.signup(registerRequest);
        return new ResponseEntity<>("User Registration successful", OK);
    }

    /**
     * Endpoint to verify account
     * @param token The token sent to User to verify account
     * @return Response Entity of verification result
     */
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verifyAccount(@PathVariable String token){

        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully",OK);
    }

    @PostMapping("/login")
    public AuthenticationResponseDto Login(@RequestBody LoginRequestDto loginRequestDto ){

        return authService.login(loginRequestDto);


    }

}
