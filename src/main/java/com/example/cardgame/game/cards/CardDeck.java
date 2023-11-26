package com.example.cardgame.game.cards;

import com.example.cardgame.game.params.CardValue;
import com.example.cardgame.game.params.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CardDeck {

    private static List<Card> generateArray() {
        List<Card> cards = new ArrayList<>();

        for (CardValue value : CardValue.values()) {
            for (Suit suit : Suit.values()) {
                cards.add(new Card(value, suit));
            }
        }

        Collections.shuffle(cards);
        return cards;
    }

    public static Stack<Card> generate() {
        Stack<Card> deck = new Stack<>();
        List<Card> cards = generateArray();

        for (Card card : cards) {
            deck.push(card);
        }

        return deck;
    }
}
