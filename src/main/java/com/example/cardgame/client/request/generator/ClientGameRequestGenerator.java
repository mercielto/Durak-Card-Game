package com.example.cardgame.client.request.generator;

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
}
