package com.example.cardgame.server.exception;

import java.util.UUID;

public class NoRoomFoundException extends Exception {
    public NoRoomFoundException(String peroperty) {
        System.out.println("There is no room with \"?\" property".formatted(peroperty));
    }
}
