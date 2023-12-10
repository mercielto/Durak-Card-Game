package com.example.cardgame.server.game;

import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.gameProperties.params.Suit;
import com.example.cardgame.server.Connection;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Connection connection;
    private List<Card> cards = new ArrayList<>();

    public Player(Connection connection1) {
        connection = connection1;
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int getCardsCount() {
        return cards.size();
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card getMinValueCard(Suit suit) {
        Card min = null;
        for (Card card : cards) {
            if (card.getSuit() == suit) {
                if (min != null) {
                    if (min.getValue().isBigger(card.getValue())) {
                        min = card;
                    }
                } else {
                    min = card;
                }
            }
        }
        return min;
    }

    public void write(String text) {
        connection.write(text);
    }

    public Connection getConnection() {
        return connection;
    }
}
