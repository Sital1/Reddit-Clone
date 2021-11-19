package com.example.redditclonebackend.exceptions;

public class SubRedditNotFoundException extends RuntimeException  {

    public SubRedditNotFoundException(String message) {
        super(message);

    }
}
