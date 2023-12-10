package com.example.cardgame.server.responseGenerator;

import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.GameCommands;

import java.util.StringJoiner;

public class ServerGameListenerResponseGenerator {
    public static String yourMove() {
        return GameCommands.YOUR_MOVE.getValue();
    }

    public static String notYourMoveYet() {
        return GameCommands.NOT_YOUR_MOVE_YET.getValue();
    }

    public static String newCardOnTable(String string) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(GameCommands.NEW_CARD_ON_TABLE.getValue());
        joiner.add(string);
        return joiner.toString();
    }
}
