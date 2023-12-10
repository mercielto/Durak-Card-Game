package com.example.cardgame.server.service;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.request.generator.ClientGameRequestGenerator;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.game.DurakGame;
import com.example.cardgame.server.game.Player;
import com.example.cardgame.server.responseGenerator.ServerGameListenerResponseGenerator;

import java.util.List;

public class ServerGameService {
    public static void newCardOnTable(Card card) {
        Client client = ClientSingleton.getClient();
        client.write(
                ClientGameRequestGenerator.newCardOnTable(card.toString())
        );
    }

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

        room.sendMessageToAll(
                ServerGameListenerResponseGenerator.newCardOnTable(card.toString()),
                List.of(player.getConnection())
        );
    }
}
