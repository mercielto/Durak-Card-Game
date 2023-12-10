package com.example.cardgame.server.timerTask;

import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.responseGenerator.ServerMainListenerResponseGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class ConnectionReadyDurationTask extends TimerTask {
    private final Room room;

    public ConnectionReadyDurationTask(Room room1) {
        room = room1;
    }

    @Override
    public void run() {
        List<Connection> readyPLayers = room.getReadyPlayers();

        if (room.isGameIsOn()) {
            return;
        }

        if (readyPLayers.size() != room.getPlayersCount()) {
            for (Connection connection : new ArrayList<>(room.getConnections())) {
                if (!readyPLayers.contains(connection)) {
                    room.removeConnection(connection);
                    connection.write(
                            ServerMainListenerResponseGenerator.leaveRoom()
                    );
                    System.out.println("%s was removed from %s room".formatted(
                            connection.getName(),
                            room.getRoomName())
                    );
                }
            }
            room.clearReadyPlayers();

        }
    }
}
