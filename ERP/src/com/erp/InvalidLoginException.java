package com.erp;

public class InvalidLoginException extends Exception {
    public InvalidLoginException(String username) {
        super("Invalid login attempt for user: " + username);
    }
}