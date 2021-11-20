package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.config.security.JwtProvider;
import com.example.redditclonebackend.config.security.SecurityUser;
import com.example.redditclonebackend.dto.AuthenticationResponseDto;
import com.example.redditclonebackend.dto.LoginRequestDto;
import com.example.redditclonebackend.dto.RefreshTokenDto;
import com.example.redditclonebackend.dto.RegisterRequestDTO;
import com.example.redditclonebackend.exceptions.SpringRedditException;
import com.example.redditclonebackend.model.NotificationEmail;
import com.example.redditclonebackend.model.User;
import com.example.redditclonebackend.model.VerificationToken;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.repository.VerificationTokenRepository;
import com.example.redditclonebackend.service.AuthService;
import com.example.redditclonebackend.service.MailService;
import com.example.redditclonebackend.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    // Lombok takes care of creating constructor.



    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * Register a new user.
     * @param registerRequestDTO RegisterRequestDTO object that contains username and password
     */
    @Override
    @Transactional
    public void signup(RegisterRequestDTO registerRequestDTO) {

        // map thr DTO to actual model
        User user = modelMapper.map(registerRequestDTO, User.class);

        // encode the password
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        // when the user created
        user.setCreated(Instant.now());

        // disable the user by default. Once user is validated then true.
        user.setEnabled(false);

        userRepository.save(user);

        // send account activation email
        // generate token and send that token to the email
        // user verified then enable the user

        String token = generateVerificationToken(user);
        System.out.println(token);
        System.out.println("http://localhost:8080/api/auth/accountVerification/"+ token);
        mailService.sendEmail(new NotificationEmail("Please Activate Your Account", user.getEmail(), "Thankyoy for signing up" +
                "please click on the link below to activate the account :" +
                "http://localhost:8080/api/auth/accountVerification/"+ token));

    }

    /**
     * Business logic to verify token
     * @param token Token Sent to user's email
     */
    @Override
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);

        verificationTokenOptional.orElseThrow(()-> new SpringRedditException("Invalid Token"));
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    /**
     * Attemps user Login if the credentials match
     * @param loginRequest The object containing username and password.
     * @return The AuhtenticationResponseDto object
     */

    public AuthenticationResponseDto login(LoginRequestDto loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String authenticationToken = jwtProvider.generateToken(authenticate);
        return  AuthenticationResponseDto.builder()
                .username(loginRequest.getUsername())
                .authenticationToken(authenticationToken)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTime()))
                .build();
    }



    /**
     * Fetches user from the provided token object and enables the user if the user exists
     * @param verificationToken verificationToken object
     */
    @Transactional
    void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();

        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new SpringRedditException("User not found: " + username));

        // user exists and now is enabled to use the account
        user.setEnabled(true);

        userRepository.save(user);
    }

    /**
     * Returns the current user
     * @return Current User object
     */
    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser(){
        SecurityUser principal = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findUserByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Use name not found - " + principal.getUsername() ));

    }

    /**
     * Generates a new refresh token.
     * @param refreshTokenDto The RefreshTokenDto object.
     * @return The AuthenticationResponseDto with a generated refresh token.
     */
    @Override
    public AuthenticationResponseDto refreshToken(RefreshTokenDto refreshTokenDto) {
        // validate the current refresh token
        refreshTokenService.validateRefreshToken(refreshTokenDto.getRefreshToken());

        // only executes if the refresh token is valid.
        String token = jwtProvider.generateTokenWithUsername(refreshTokenDto.getUsername());

        return  AuthenticationResponseDto.builder()
                .username(refreshTokenDto.getUsername())
                .authenticationToken(token)
                .refreshToken(refreshTokenDto.getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTime()))
                .build();
    }


    /**
     * Generates a random token to be sent to the user in activation email and persists the token in the database.
     * @param user The user for whom the token is generated
     * @return The token
     */

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();

        // persist the object into the database.

        VerificationToken verificationToken = new VerificationToken();

        // pass the token and the user to class
         verificationToken.setToken(token);
         verificationToken.setUser(user);
         // save the verification token
        verificationTokenRepository.save(verificationToken);

        return token;
    }
}
