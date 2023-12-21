package com.example.cardgame.client;

import com.example.cardgame.client.response.model.RoomResponse;

public class ClientRoomSingleton {
    public static RoomResponse room;

    public static RoomResponse getRoom() {
        return room;
    }

    public static void setRoom(RoomResponse roomResponse) {
        System.out.println("CLIENT ROOM SET TO %S".formatted(roomResponse));
        room = roomResponse;
    }
}
