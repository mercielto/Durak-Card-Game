package com.example.cardgame.gameProperties.exception;

public class CommandNotDefinedException extends RuntimeException {
    public CommandNotDefinedException(String text) {
        System.out.println("Command (%s) received from server don't match any defined commands.".formatted(text));
    }
}
