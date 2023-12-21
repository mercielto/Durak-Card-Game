package com.example.cardgame.gameProperties.cards;

import com.example.cardgame.gameProperties.params.Suit;

import java.util.List;

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

    public boolean isFull() {
        return first != null && second != null;
    }

    public List<Card> getAll() {
        if (second != null) {
            return List.of(first, second);
        }
        return List.of(first);
    }
}
