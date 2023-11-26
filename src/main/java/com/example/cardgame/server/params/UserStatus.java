package com.example.cardgame.server.params;

public enum UserStatus {

    LOOKING_FOR_ROOM(1), IN_ROOM(2), IN_GAME(3);

    private int value;
    UserStatus(int v) {
        value = v;
    }
}
