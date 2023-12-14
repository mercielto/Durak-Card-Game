package com.example.cardgame.server.service;

import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.game.DurakGame;
import com.example.cardgame.server.game.Player;
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

            game.resetEndedMovesPLayersCount();
            game.clearTable();
            Map<Player, List<Card>> dealtCards = game.dealCards();
            for (Player pl : dealtCards.keySet()) {
                pl.write(
                        ServerGameListenerResponseGenerator.endMove()
                );

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
            game.next();
            Player currentPlayer = game.getCurrentPlayer();
            currentPlayer.write(
                    ServerGameListenerResponseGenerator.yourMove()
            );
        }
    }
}
