package com.example.cardgame.gameProperties.cards;

import com.example.cardgame.gameProperties.params.CardValue;
import com.example.cardgame.gameProperties.params.Suit;

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

        // temporary // test

        cards = cards.subList(0, 7);

        for (Card card : cards) {
            deck.push(card);
        }

        return deck;
    }
}
