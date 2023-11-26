package com.example.cardgame.server.params;

public enum UserCommand {
    JOIN_ROOM(1), LEAVE_ROOM(2), CREATE_ROOM(3), START_GAME(4),
    TOSS(5), TAKE(6), PASS(7);

    private int value;
    UserCommand(int value1) {
        value = value1;
    }
}
