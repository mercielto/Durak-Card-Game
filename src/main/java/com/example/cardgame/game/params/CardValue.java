package com.example.cardgame.game.params;

public enum CardValue {
    SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(11), QUEEN(12), KING(13), ACE(14);     // валет, дама, король, туз
    private final int value;
    CardValue(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    public boolean isBigger(CardValue other) {
        return value - other.getValue() > 0;
    }
}
