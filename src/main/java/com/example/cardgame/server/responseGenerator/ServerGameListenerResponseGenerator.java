package com.example.cardgame.server.responseGenerator;

import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.GameCommands;
import com.example.cardgame.server.game.DurakGame;
import com.example.cardgame.server.game.Player;

import java.util.List;
import java.util.StringJoiner;

public class ServerGameListenerResponseGenerator {
    public static String yourMove() {
        return GameCommands.YOUR_MOVE.getValue();
    }

    public static String notYourMoveYet() {
        return GameCommands.NOT_YOUR_MOVE_YET.getValue();
    }

    public static String newCardOnTable(String string, String playerName) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.NEW_CARD_ON_TABLE.getValue());
        joiner.add(string);
        joiner.add(playerName);
        return joiner.toString();
    }

    public static String setPLayersOrder(DurakGame game) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.SET_PLAYERS_ORDER.getValue());
        StringJoiner smallerJoiner = new StringJoiner(ServerProperties.getSideDelimiter());
        for (Player player : game.getPlayers()) {
            smallerJoiner.add(player.getConnection().getName());
        }
        joiner.add(smallerJoiner.toString());
        return joiner.toString();
    }

    public static String beatCard(String name, Card cardToBeat, Card card) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.BEAT_CARD.getValue());
        joiner.add(name);
        joiner.add(cardToBeat.toString());
        joiner.add(card.toString());
        return joiner.toString();
    }

    public static String addNewCards(List<Card> cards) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.ADD_NEW_CARDS_ON_HANDS.getValue());

        for (Card card : cards) {
            joiner.add(card.toString());
        }
        return joiner.toString();
    }

    public static String endMove() {
        return GameCommands.END_MOVE.getValue();
    }

    public static String addNewCardsToPlayer(String name, int size) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.ADD_NEW_CARDS_TO_USER.getValue());
        joiner.add(name);
        joiner.add(String.valueOf(size));
        return joiner.toString();
    }

    public static String playerTookCards() {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.TAKE_CARDS.getValue());
        return joiner.toString();
    }

    public static String playerWonTheGame(Player player) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.PLAYER_WON_THE_GAME.getValue());
        joiner.add(player.getConnection().getName());
        return joiner.toString();
    }

    public static String playerQuitGame(Player player) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.QUIT_GAME_NOTIFICATION.getValue());
        joiner.add(player.getConnection().getName());
        return joiner.toString();
    }

    public static String fool(String name) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.FOOL.getValue());
        joiner.add(name);
        return joiner.toString();
    }

    public static String draw() {
        return GameCommands.DRAW.getValue();
    }
}
