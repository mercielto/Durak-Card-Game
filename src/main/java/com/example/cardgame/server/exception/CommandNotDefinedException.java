package com.example.cardgame.server.exception;

public class CommandNotDefinedException extends RuntimeException {
    public CommandNotDefinedException(String text) {
        System.out.println("Command (%s) received from client don't match any defined commands.".formatted(text));
    }
}
