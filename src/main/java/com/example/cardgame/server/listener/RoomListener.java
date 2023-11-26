package com.example.cardgame.server.listener;

import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Room;

public class RoomListener extends Thread {
    private Connection connection;
    private Room room;

    public RoomListener(Connection connection1, Room room1) {
        connection = connection1;
        room = room1;
    }

    @Override
    public void run() {
        super.run();
    }
}
