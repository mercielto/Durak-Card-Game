package com.example.cardgame.gameProperties.params;

import com.example.cardgame.gameProperties.exception.CardValueIsNotDefined;

public enum CardValue {
    SIX(6, "6"),
    SEVEN(7, "7"),
    EIGHT(8, "8"),
    NINE(9, "9"),
    TEN(10, "10"),
    JACK(11, "jack"),
    QUEEN(12, "queen"),
    KING(13, "king"),
    ACE(14, "ace");

    private final int value;
    private final String name;

    CardValue(int value1, String name1) {
        value = value1;
        name = name1;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getSendValue() {
        return Integer.toString(value - 6);
    }

    public static CardValue defineRequestValue(String val) throws CardValueIsNotDefined {
        for (CardValue cardV : values()) {
            if (cardV.value - 6 == Integer.parseInt(val)) {
                return cardV;
            }
        }
        throw new CardValueIsNotDefined(val);
    }

    public boolean isBigger(CardValue other) {
        return value - other.getValue() > 0;
    }
}
