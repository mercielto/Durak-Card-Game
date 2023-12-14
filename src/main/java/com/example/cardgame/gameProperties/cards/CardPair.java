package com.example.cardgame.gameProperties.cards;

import com.example.cardgame.gameProperties.params.Suit;

public class CardPair {
    private Card first;
    private Card second;
    private Suit trumpSuit;

    public CardPair(Card f, Suit s) {
        first = f;
        trumpSuit = s;
    }

    public boolean beats(Card secondCard) {
        if (second != null) {
            return false;
        }

        if ((first.getSuit() == secondCard.getSuit() && secondCard.getValue().isBigger(first.getValue()) )
                || (secondCard.getSuit() == trumpSuit && first.getSuit() != trumpSuit)) {
            second = secondCard;
            return true;
        }
        return false;
    }

    public Card getFirst() {
        return first;
    }

    public Card getSecond() {
        return second;
    }
}
