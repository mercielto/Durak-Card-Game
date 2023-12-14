package com.example.cardgame.server.game;

import java.util.*;

import com.example.cardgame.gameProperties.cards.*;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.exception.PlayerNotFoundException;
import com.example.cardgame.server.listener.ServerGameListener;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.responseGenerator.ServerGameListenerResponseGenerator;
import com.example.cardgame.server.timerTask.PlayerMoveDurationTask;

public class DurakGame {
    private Stack<Card> deck = CardDeck.generate();
    private List<CardPair> cardsOnTable = new ArrayList();

    private List<Player> players;
    private Card trumpCard;
    private int currentPlayer = 0;
    private Room room;

    public static final int maxCardsOnTable = 6;
    public static final int maxCardCount = 6;
    public static final int maxPlayersSize = 6;
    public static final int minPlayersSize = 2;
    public static final int timePerMove = 20000; // in milliseconds

    private int endedMovesPLayersCount = 0;


    public DurakGame(Room room) {
        List<Connection> connections = room.getConnections();
        this.room = room;

        // creating game.Player based on server.Connection
        players = new ArrayList<>();
        for (Connection connection : connections) {
            players.add(new Player(connection));
        }

        // handing out cards
        for (Player player : players) {
            while (player.getCardsCount() < maxCardCount) {
                player.addCard(deck.pop());
            }
        }

        trumpCard = deck.pop();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayer);
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
        room.sendMessageToAll(
                ServerGameListenerResponseGenerator.setPLayersOrder(this)
        );
        players.get(currentPlayer).write(
            ServerGameListenerResponseGenerator.yourMove()
        );

//        Player player = getCurrentPlayer();
//        Timer timer = new Timer();
//        timer.schedule(new PlayerMoveDurationTask(this, player), timePerMove);
//        // int message = Integer.parseInt(player.read());
//        timer.cancel();
//        timer.purge();
//        currentPlayer = (currentPlayer + 1) % players.size();
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(Connection connection) throws PlayerNotFoundException {
        for (Player player : players) {
            if (player.getConnection() == connection) {
                return player;
            }
        }
        throw new PlayerNotFoundException(connection.getName());
    }

    public Card getTrumpCard() {
        return trumpCard;
    }

    public int getCardsOnTableCount() {
        return cardsOnTable.size();
    }

    public Set<Player> canMoveCards() {
        Set<Player> playerSet = new HashSet<>();
        playerSet.add(players.get(currentPlayer));
        playerSet.add(players.get((currentPlayer + 2) % players.size()));
        return playerSet;
    }

    public void moveCard(Player player, Card card) {
        if (canMoveCards().contains(player)) {
            player.removeCard(card);
            cardsOnTable.add(
                    new CardPair(card, trumpCard.getSuit())
            );
        }
    }

    public CardPair getCardPair(Card card) {
        for (CardPair cardPair : cardsOnTable) {
            if (cardPair.getFirst().equals(card)) {
                return cardPair;
            }
        }
        throw new RuntimeException("There is no CardPair: " + card.toString());
    }

    public boolean beatCard(Card cardToBeat, Card card) {
        CardPair pair = getCardPair(cardToBeat);
        return pair.beats(card);
    }

    public Room getRoom() {
        return room;
    }

    public Player getDefenderPlayer() {
        int pos = (currentPlayer + 1) % players.size();
        return players.get(pos);
    }

    public void resetEndedMovesPLayersCount() {
        this.endedMovesPLayersCount = 0;
    }

    public void addNewEndedMovesPLayer() {
        endedMovesPLayersCount++;
    }

    public boolean isAllEndedMove() {
        return (players.size() - 1) == endedMovesPLayersCount;
    }

    public void clearTable() {
        cardsOnTable.clear();
    }

    public List<Card> dealCardToPlayer(Player player) {
        List<Card> cards = new ArrayList<>();
        int cardsCount = player.getCardsCount();
        for (int i = 0; i < maxCardCount - cardsCount; i++) {
            Card card = deck.pop();
            cards.add(card);
            player.addCard(card);
        }
        return cards;
    }

    public Map<Player, List<Card>> dealCards() {
        Map<Player, List<Card>> dealtCards = new HashMap<>();

        int previousPosition = currentPlayer - 1;
        if (previousPosition < 0) {
            previousPosition += players.size();
        }

        int nextPosition = (currentPlayer + 1) % players.size();

        for (int playerId = currentPlayer; playerId != previousPosition;
             playerId = (playerId + 1) % players.size()) {

            if (playerId == nextPosition) {
                continue;
            }

            Player player = players.get(playerId);
            List<Card> cards = dealCardToPlayer(player);

            dealtCards.put(player, cards);
        }

        // defender gets cards at the end of a deal
        Player player = players.get(nextPosition);
        List<Card> cards = dealCardToPlayer(player);
        dealtCards.put(player, cards);

        return dealtCards;
    }

    public void next() {
        currentPlayer = (currentPlayer + 1) % players.size();
    }
}
