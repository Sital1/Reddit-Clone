package com.example.redditclonebackend.config.security;

import com.example.redditclonebackend.exceptions.SpringRedditException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;

@Service
public class JwtProvider {

    private KeyStore keyStore;

    @Value("${secret}")
    private String secret;
    
    private JwtParser parser;

    /**
     * loads the keystore from json key store
     */

    @PostConstruct
    public void init(){
        try{
            keyStore = KeyStore.getInstance("JKS");
            InputStream resourceAsStream = getClass().getResourceAsStream("/redditclone.jks");
            keyStore.load(resourceAsStream,secret.toCharArray());


        } catch (KeyStoreException| CertificateException| IOException| NoSuchAlgorithmException e) {
            throw new SpringRedditException("Exception occured while reading keystore");
        }
    }


    /**
     * Generates Json web token from the authentication principal
     * @param authentication The authentication object
     * @return
     */
    public String generateToken(Authentication authentication){
        SecurityUser principal = (SecurityUser)authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .signWith(getPrivateKey())
                .compact();
    }


    /**
     * Gets the private key from the JKS.
     * @return The private key.
     */
    private PrivateKey getPrivateKey() {
        try {
            return (PrivateKey) keyStore.getKey("redditclone", secret.toCharArray());
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException e) {
            throw new SpringRedditException("Exception occurred while retrieving public key from keystore");
        }
    }

    /**
     * Validates the JWT token.
     * @param jwt The jwt token.
     * @return True if the token is validated
     */
    public boolean validateToken(String jwt){
        try
        {
            Jwts.parserBuilder()
                    .setSigningKey(getPublicKey())
                    .build()
                    .parse(jwt);
        }catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException e)
        {
            e.printStackTrace();
            throw new SpringRedditException("Invalid Token");
        }
        return true;
    }

    /**
     * Gets the publickey from the JKS.
     * @return
     */
    private PublicKey getPublicKey() {
        try{
            return keyStore.getCertificate("redditclone").getPublicKey();
        }catch (KeyStoreException e){
            throw new SpringRedditException("Exception occured while retreiving public key");
        }
    }

    public String getUsernameFromJwt(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getPrivateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();

    }


}
