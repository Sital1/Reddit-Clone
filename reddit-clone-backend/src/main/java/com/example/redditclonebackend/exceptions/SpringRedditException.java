package com.example.redditclonebackend.exceptions;

public class SpringRedditException extends RuntimeException {

    /**
     * Creates a custom exception when email cannot be sent.
     * @param exMessage The exception message.
     */
    // Exception occurs in backend frequently, and we do not want to show any technical details
    public SpringRedditException(String exMessage) {
        super(exMessage);
    }
}
