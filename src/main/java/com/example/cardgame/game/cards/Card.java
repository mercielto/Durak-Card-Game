package com.example.cardgame.game.cards;

import com.example.cardgame.game.params.CardValue;
import com.example.cardgame.game.params.Suit;

public class Card {
    private final CardValue value;
    private final Suit suit;

    public Card(CardValue v, Suit s) {
        value = v;
        suit = s;
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
}
