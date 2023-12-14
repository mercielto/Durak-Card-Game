package com.example.cardgame.properties.commands;

import com.example.cardgame.gameProperties.exception.CommandNotDefinedException;

public enum GameCommands {
    YOUR_MOVE("0"),
    NEW_CARD_ON_TABLE("1"),
    NOT_YOUR_MOVE_YET("2"),
    SET_PLAYERS_ORDER("3"),
    BEAT_CARD("4"),
    END_MOVE("5"),
    ADD_NEW_CARDS("6"),
    ADD_NEW_CARDS_TO_USER("7");

    private final String value;

    GameCommands(String v) {
        value = v;
    }

    public static GameCommands defineCommand(String s) {
        for (GameCommands command : values()) {
            if (command.getValue().equals(s)) {
                return command;
            }
        }
        throw new CommandNotDefinedException(s);
    }

    public String getValue() {
        return value;
    }
}
