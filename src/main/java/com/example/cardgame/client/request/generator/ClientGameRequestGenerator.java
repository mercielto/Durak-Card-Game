package com.example.cardgame.client.request.generator;

import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.GameCommands;

import java.util.StringJoiner;

public class ClientGameRequestGenerator {
    public static String newCardOnTable(String card) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.NEW_CARD_ON_TABLE.getValue());
        joiner.add(card);
        return joiner.toString();
    }

    public static String beatCard(Card cardToBeat, Card selectedCard) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.BEAT_CARD.getValue());
        joiner.add(cardToBeat.toString());
        joiner.add(selectedCard.toString());
        return joiner.toString();
    }

    public static String endMove() {
        return GameCommands.END_MOVE.getValue();
    }

    public static String takeCards() {
        return GameCommands.TAKE_CARDS.getValue();
    }

    public static String noMoreCardsOnHands() {
        return GameCommands.NO_MORE_CARDS_ON_HANDS.getValue();
    }

    public static String forcedEndOfMove() {
        return GameCommands.FORCED_END_OF_MOVE.getValue();
    }

    public static String quitGame() {
        return GameCommands.QUIT_GAME.getValue();
    }
}
