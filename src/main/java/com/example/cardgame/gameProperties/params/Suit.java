package com.example.cardgame.gameProperties.params;

import com.example.cardgame.server.exception.SuitIsNotDefined;

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

    public String getSendValue() {
        return name.substring(1, 2);
    }

    public static Suit defineRequestValue(String val) throws SuitIsNotDefined {
        for (Suit suit : values()) {
            if (suit.name.substring(1, 2).equals(val)) {
                return suit;
            }
        }
        throw new SuitIsNotDefined(val);
    }
}
