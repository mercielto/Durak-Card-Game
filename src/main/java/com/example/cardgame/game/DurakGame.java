package com.example.cardgame.game;

import java.util.*;

import com.example.cardgame.game.cards.*;
import com.example.cardgame.server.listener.GameListener;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.timerTask.PlayerMoveDurationTask;

public class DurakGame {
    private Stack<Card> deck = CardDeck.generate();
    private List<CardPair> cardsOnTable = new ArrayList();

    private List<Player> players;
    private Card trumpCard;
    private int currentPlayer = 0;

    public static final int maxCardsOnTable = 6;
    public static final int maxCardCount = 6;
    public static final int maxPlayersSize = 6;
    public static final int minPlayersSize = 2;
    public static final int timePerMove = 20000; // in milliseconds

    public DurakGame(List<Connection> connections) {
        // creating game.Player based on server.Connection
        players = new ArrayList<>();
        for (Connection connection : connections) {
            connection.setListener(new GameListener(connection, this));
            players.add(new Player(connection));
        }

        // handing out cards
        for (Player player : players) {
            while (player.getCardsCount() < maxCardCount) {
                player.addCard(deck.pop());
            }
        }

        trumpCard = deck.pop();

        start();
    }

    private Player getPlayer() {
        Player pl = players.get(currentPlayer);
        currentPlayer = (currentPlayer + 1) % players.size();
        return pl;
    }

    private void orderPlayersByTrumpSuit() {
        players.sort(
                (o1, o2) -> {
                    Card first = o1.getMinValueCard(trumpCard.getSuit());
                    Card second = o2.getMinValueCard(trumpCard.getSuit());

                    if (first == null && second == null) {
                        return 0;
                    } else if (first == null) {
                        return 1;
                    } else if (second == null) {
                        return -1;
                    } else {
                        return first.getValue().getValue() - second.getValue().getValue();
                    }
                }
        );
    }

    public void start() {
        orderPlayersByTrumpSuit();
        changePlayersListener();

        while (players.size() >= 2) {
            Player player = getPlayer();
            player.write("");           // какое-то сообщение об отправке
            Timer timer = new Timer();
            timer.schedule(new PlayerMoveDurationTask(this, player), timePerMove);
            // int message = Integer.parseInt(player.read());
            timer.cancel();
            timer.purge();
        }
    }

    private void changePlayersListener() {
        for (Player player : players) {
            Connection playerConnection = player.getConnection();
            playerConnection.setListener(new GameListener(
                    playerConnection, this
            ));
        }
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }
}
