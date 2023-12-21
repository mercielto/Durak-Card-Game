package com.example.cardgame.server.service;

import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.game.DurakGame;
import com.example.cardgame.server.game.Player;
import com.example.cardgame.server.listener.ServerMainListener;
import com.example.cardgame.server.responseGenerator.ServerGameListenerResponseGenerator;

import java.util.List;
import java.util.Map;

public class ServerGameService {

    public static void handleNewCardOnTable(String[] splitMessage, Player player, Room room) {
        DurakGame game = room.getGame();
        if (!game.canMoveCards().contains(player)) {
            player.write(
                    ServerGameListenerResponseGenerator.notYourMoveYet()
            );
            return;
        }

        Card card = Card.getCard(splitMessage[1]);
        game.moveCard(player, card);
        game.resetEndedMovesPLayersCount();

        room.sendMessageToAll(
                ServerGameListenerResponseGenerator.newCardOnTable(
                        card.toString(),
                        player.getConnection().getName()
                ),
                List.of(player.getConnection())
        );
    }

    public static void handleBeatCard(DurakGame game, Player player, String[] splitMessage) {
        Card cardToBeat = Card.getCard(splitMessage[1]);
        Card card = Card.getCard(splitMessage[2]);

        player.removeCard(card);
        if (game.beatCard(cardToBeat, card)) {
            Room room = game.getRoom();
            room.sendMessageToAll(
                    ServerGameListenerResponseGenerator.beatCard(player.getConnection().getName(),
                            cardToBeat, card),
                    List.of(player.getConnection())
            );
        }
    }

    public static void handleEndMove(DurakGame game, Player player) {
        game.addNewEndedMovesPLayer();

        if (game.getCurrentPlayer().equals(player)) {
            Room room = game.getRoom();
            room.sendMessageToConnectionsInGame(
                    ServerGameListenerResponseGenerator.yourMove(),
                    List.of(player, game.getDefenderPlayer())
            );
        }

        if (game.isAllEndedMove()) {
            startNewCycle(game);
        }
    }

    public static void endGame(Room room) {
        for (Connection connection : room.getConnections()) {
            connection.setListener(new ServerMainListener(connection));
        }
        room.deleteGame();
    }

    private static void startNewCycle(DurakGame game) {
        int count = game.howManyPlayersLeft();
        Room room = game.getRoom();
        if (count == 1) {
            Player player = game.getPlayers().get(0);
            room.sendMessageToConnectionsInGame(
                    ServerGameListenerResponseGenerator.fool(player.getConnection().getName())
            );
            endGame(room);
            return;
        }

        if (count == 0) {
            room.sendMessageToConnectionsInGame(
                    ServerGameListenerResponseGenerator.draw()
            );
            endGame(room);
            return;
        }

        game.resetEndedMovesPLayersCount();
        game.clearTable();
        sendEndMoves(game);
        dealCards(game);
        game.next();
        Player currentPlayer = game.getCurrentPlayer();
        currentPlayer.write(
                ServerGameListenerResponseGenerator.yourMove()
        );
    }

    private static void sendEndMoves(DurakGame game) {
        game.getRoom().sendMessageToConnectionsInGame(
                ServerGameListenerResponseGenerator.endMove()
        );
    }

    private static Map<Player, List<Card>> dealCards(DurakGame game) {
        Map<Player, List<Card>> dealtCards = game.dealCards();
        for (Player pl : dealtCards.keySet()) {
            pl.write(
                    ServerGameListenerResponseGenerator.addNewCards(dealtCards.get(pl))
            );

            game.getRoom().sendMessageToConnectionsInGame(
                    ServerGameListenerResponseGenerator.addNewCardsToPlayer(
                            pl.getConnection().getName(),
                            dealtCards.get(pl).size()
                    ),
                    List.of(pl)
            );
        }
        return dealtCards;
    }

    public static void handleTakeCards(DurakGame game, Player player) {
        Room room = game.getRoom();
        room.sendMessageToConnectionsInGame(
                ServerGameListenerResponseGenerator.playerTookCards(),
                List.of(player)
        );
        game.next();
        game.next();        // skip move

        game.getCurrentPlayer().write(
                ServerGameListenerResponseGenerator.yourMove()
        );

        dealCards(game);
    }

    public static void handleNoMoreCardsOnHands(DurakGame game, Player player) {
        Room room = game.getRoom();
        room.sendMessageToConnectionsInGame(
                ServerGameListenerResponseGenerator.endMove()
        );

        room.sendMessageToConnectionsInGame(
                ServerGameListenerResponseGenerator.playerWonTheGame(player)
        );

        game.removePlayer(player);
//        game.stepBack();
        startNewCycle(game);
    }

    public static void handleForcedEndOfMove(DurakGame game) {
        game.getRoom().sendMessageToConnectionsInGame(
                ServerGameListenerResponseGenerator.endMove()
        );
        startNewCycle(game);
    }

    public static void handleQuit(DurakGame game, Player player) {
        Room room = game.getRoom();
        game.removePlayer(player);
        room.sendMessageToConnectionsInGame(
                ServerGameListenerResponseGenerator.playerQuitGame(player)
        );
        player.getConnection().setListener(
                new ServerMainListener(player.getConnection()
                )
        );

        List<Player> players = game.getPlayers();
        if (players.size() == 1) {
            room.sendMessageToConnectionsInGame(
                    ServerGameListenerResponseGenerator.playerWonTheGame(players.get(0))
            );
        }
    }
}
