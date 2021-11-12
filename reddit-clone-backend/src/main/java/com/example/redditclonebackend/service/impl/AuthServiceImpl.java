package com.example.redditclonebackend.service.impl;

import com.example.redditclonebackend.dto.RegisterRequestDTO;
import com.example.redditclonebackend.model.NotificationEmail;
import com.example.redditclonebackend.model.User;
import com.example.redditclonebackend.model.VerificationToken;
import com.example.redditclonebackend.repository.UserRepository;
import com.example.redditclonebackend.repository.VerificationTokenRepository;
import com.example.redditclonebackend.service.AuthService;
import com.example.redditclonebackend.service.MailService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
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
        mailService.sendEmail(new NotificationEmail("Please Activate Your Account", user.getEmail(), "Thankyoy for signing up" +
                "please click on the link below to activate the account :" +
                "http://localhost:8080/api/auth/accountVerification/"+ token));

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

         // save the verification token
        verificationTokenRepository.save(verificationToken);

        return token;
    }
}
