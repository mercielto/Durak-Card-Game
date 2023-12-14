package com.example.cardgame.properties.commands;

import com.example.cardgame.gameProperties.exception.CommandNotDefinedException;

import java.util.Objects;

public enum MenuCommands {
    JOIN_ROOM("1"),
    LEAVE_ROOM("2"),
    CREATE_ROOM("3"),
    START_GAME("4"),
    GET_AVAILABLE_ROOMS("5"),
    ERROR("0"),
    SOMEONE_JOINED_ROOM("6"),
    GET_PLAYERS_LIST_VIEW("7"),
    WAITING_FOR_CONFIRMATION("8"),
    READY("9"),
    SET_NAME("10");

    private String value;
    MenuCommands(String v) {
        value = v;
    }

    public static MenuCommands defineCommand(String val) {
        for (MenuCommands command : MenuCommands.values()) {
            if (Objects.equals(command.value, val)) {
                return command;
            }
        }
        throw new CommandNotDefinedException(String.valueOf(val));
    }

    public String getValue() {
        return value;
    }
}
