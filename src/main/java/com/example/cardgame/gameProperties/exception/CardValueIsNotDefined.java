package com.example.cardgame.gameProperties.exception;

public class CardValueIsNotDefined extends Exception {
    public CardValueIsNotDefined(String val) {
        System.out.println("CardValue can not define %s".formatted(val));
    }
}
