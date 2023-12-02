package com.example.cardgame.server.exception;

import java.util.UUID;

public class NoRoomFoundException extends Exception {
    public NoRoomFoundException(UUID uuid) {
        System.out.println("There is no room with \"?\" uuid".formatted(uuid));
    }
}
