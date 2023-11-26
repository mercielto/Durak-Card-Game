package com.example.cardgame.game.params;

public enum Suit {
    HEARTS("hearts"),
    SPADES("spades"),
    DIAMONDS("diamonds"),
    CLUBS("clubs");

    private final String name;

    Suit(String name1) {
        name = name1;
    }

    public String getName() {
        return name;
    }
}
