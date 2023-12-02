package com.example.cardgame.properties.commands;

import com.example.cardgame.server.exception.CommandNotDefinedException;

public enum MenuCommands {
    JOIN_ROOM(1), LEAVE_ROOM(2), CREATE_ROOM(3), START_GAME(4),
    GET_AVAILABLE_ROOMS(5);

    private int value;
    MenuCommands(int v) {
        value = v;
    }

    public static MenuCommands defineCommand(int val) {
        for (MenuCommands command : MenuCommands.values()) {
            if (command.value == val) {
                return command;
            }
        }
        throw new CommandNotDefinedException(String.valueOf(val));
    }

    public int getValue() {
        return value;
    }
}
