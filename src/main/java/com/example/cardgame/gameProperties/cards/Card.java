package com.example.cardgame.gameProperties.cards;

import com.example.cardgame.gameProperties.params.CardValue;
import com.example.cardgame.gameProperties.params.Suit;
import com.example.cardgame.gameProperties.exception.CardValueIsNotDefined;
import com.example.cardgame.server.exception.SuitIsNotDefined;

public class Card {
    private final CardValue value;
    private final Suit suit;
    private final String imageName;

    public Card(CardValue value1, Suit suit1) {
        value = value1;
        suit = suit1;
        imageName = "%s_of_%s.png".formatted(value1.getName(), suit1.getName());
    }

    public CardValue getValue() {
        return value;
    }

    public Suit getSuit() {
        return suit;
    }

    public boolean equals(Card other) {
        return other.getValue() == getValue() && other.getSuit() == suit;
    }

    @Override
    public String toString() {
        return value.getSendValue() + suit.getSendValue();
    }

    public static Card getCard(String text) {
        CardValue v = null;
        try {
            v = CardValue.defineRequestValue(String.valueOf(text.charAt(0)));
            Suit s = Suit.defineRequestValue(String.valueOf(text.charAt(1)));
            return new Card(v, s);
        } catch (CardValueIsNotDefined | SuitIsNotDefined e) {
            throw new RuntimeException(e);
        }
    }

    public String getImageName() {
        return imageName;
    }
}
