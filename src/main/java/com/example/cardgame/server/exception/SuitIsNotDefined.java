package com.example.cardgame.server.exception;

public class SuitIsNotDefined extends Exception {
    public SuitIsNotDefined(String val) {
        System.out.println("Suit can't define %s".formatted(val));
    }
}
