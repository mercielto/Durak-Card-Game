package com.example.cardgame.server.exception;

public class PlayerNotFoundException extends Exception {
    public PlayerNotFoundException(String name) {
        System.out.println("Player '%s' is not found.".formatted(name));
    }
}
