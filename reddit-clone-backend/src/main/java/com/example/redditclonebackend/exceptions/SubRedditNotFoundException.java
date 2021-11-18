package com.example.redditclonebackend.exceptions;

public class SubRedditNotFoundException extends RuntimeException  {

    public SubRedditNotFoundException(String exMessage) {

        super(exMessage);
    }
}
