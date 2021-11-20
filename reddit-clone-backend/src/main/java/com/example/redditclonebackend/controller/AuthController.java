package com.example.redditclonebackend.controller;

import com.example.redditclonebackend.dto.AuthenticationResponseDto;
import com.example.redditclonebackend.dto.LoginRequestDto;
import com.example.redditclonebackend.dto.RefreshTokenDto;
import com.example.redditclonebackend.dto.RegisterRequestDTO;
import com.example.redditclonebackend.service.AuthService;
import com.example.redditclonebackend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;


    /**
     * Endpoint to handle user signup
     * @param registerRequest User Data
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequestDTO registerRequest){
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

    /**
     * Endpoint to handle User login
     * @param loginRequestDto The dto object containing login request
     * @return Returns the dto containing login response.
     */
    @PostMapping("/login")
    public AuthenticationResponseDto login(@RequestBody LoginRequestDto loginRequestDto ){
        return authService.login(loginRequestDto);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponseDto refreshToken(@Valid @RequestBody RefreshTokenDto refreshTokenDto){

        return authService.refreshToken(refreshTokenDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenDto refreshTokenDto){
        refreshTokenService.deleteRefreshToken(refreshTokenDto.getRefreshToken());
        return ResponseEntity.status(OK).body("Refresh token deleted successfully");
    }

}
