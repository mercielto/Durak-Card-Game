package com.example.cardgame.game.cards;

import com.example.cardgame.game.params.Suit;

public class CardPair {
    private Card first;
    private Card second;
    private Suit trumpSuit;

    public CardPair(Card f, Suit s) {
        first = f;
        trumpSuit = s;
    }

    public boolean beat(Card secondCard) {
        if (second != null) {
            return false;
        }

        if (first.getSuit() == secondCard.getSuit() && first.getValue().isBigger(secondCard.getValue()) ||
            secondCard.getSuit() == trumpSuit) {
            second = secondCard;
            return true;
        }
        return false;
    }
}
