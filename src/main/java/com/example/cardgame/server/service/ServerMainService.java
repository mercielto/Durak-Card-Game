package com.example.cardgame.server.service;

import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.Server;
import com.example.cardgame.server.ServerSingleton;
import com.example.cardgame.server.exception.NoRoomFoundException;
import com.example.cardgame.server.model.request.RoomRequest;
import com.example.cardgame.server.responseGenerator.ServerMainListenerResponseGenerator;
import com.example.cardgame.server.timerTask.ConnectionReadyDurationTask;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ServerMainService {
    public static List<Room> getAvailableRooms(String[] split) {
        SearchTypeProperty typeProperty;
        String value;
        if (split.length == 1) {
            typeProperty = SearchTypeProperty.GET_ALL;
            value = "";
        } else {
            typeProperty = SearchTypeProperty.defineByRequestValue(split[1]);
            value = split[2];
        }
        return ServerSingleton.getServer().getAvailableRooms(typeProperty, value);
    }

    public static void handleJoinRoom(Connection connection, String[] splitMessage) {
        Server server = ServerSingleton.getServer();
        UUID uuid = UUID.fromString(splitMessage[1]);
        Room room = null;
        try {
            room = server.getRoomByUuid(uuid);
            if (!room.isFull() && !room.contains(connection)) {
                room.sendMessageToAll(
                        ServerMainListenerResponseGenerator.someoneJoinedRoom(connection.getName())
                );
                room.addConnection(connection);
                connection.write(
                        ServerMainListenerResponseGenerator.getRoomPlayersNames(uuid.toString())
                );

                if (room.isFull()) {
                    room.sendMessageToAll(
                            ServerMainListenerResponseGenerator.waitingForConfirmationToStartGame()
                    );

                    Timer timer = new Timer("Waiting for confirmation");
                    timer.schedule(
                            new ConnectionReadyDurationTask(room), 20000
                    );
                }
            }
        } catch (NoRoomFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleLeaveRoom(Connection connection) {
        Server server = ServerSingleton.getServer();
        try {
            Room room = server.getRoomByConnection(connection);
            room.removeConnection(connection);
            room.sendMessageToAll(
                    ServerMainListenerResponseGenerator.getRoomPlayersNames(room.getUuid().toString())
            );

            if (room.getConnections().size() == 0) {
                server.deleteRoom(room);
            }
        } catch (NoRoomFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static void handleRoomCreation(Connection connection, String[] splitMessage) {
        Server server = ServerSingleton.getServer();
        RoomRequest request = RoomRequest.builder()
                .maxPlayersCount(Integer.parseInt(splitMessage[1]))
                .name(splitMessage.length == 3 ? splitMessage[2] : null)
                .build();
        String uuid = server.createRoom(connection, request);
        connection.write(
                ServerMainListenerResponseGenerator.createRoom(uuid)
        );
    }

    public static void handleGetAvailableRooms(Connection connection, String[] splitMessage) {
        List<Room> rooms = ServerMainService.getAvailableRooms(splitMessage);
        connection.write(
                ServerMainListenerResponseGenerator.getAvailableRooms(rooms)
        );
    }

    public static void handleGetPlayersListView(Connection connection, String[] splitMessage) {
        try {
            String id = splitMessage[1];
            connection.write(
                    ServerMainListenerResponseGenerator.getRoomPlayersNames(id)
            );
        } catch (NoRoomFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleReady(Connection connection) {
        try {
            Server server = ServerSingleton.getServer();
            Room room = server.getRoomByConnection(connection);
            room.setReadyPlayer(connection);

            if (room.isEveryoneReady()) {
                room.startGame();
                for (Connection conn : room.getConnections()) {
                    conn.write(
                            ServerMainListenerResponseGenerator.startGame(room.getGame(), conn)
                    );
                }

                // на клиенте идёт смена listener, из-за чего ему нужно чуть больше времени
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        room.getGame().start();
                    }
                }, 2000);
            }

        } catch (NoRoomFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setName(Connection connection, String name) {
        Server server = ServerSingleton.getServer();
        for (Connection conn : server.getConnections()) {
            if (conn.getName().equals(name)) {
                connection.write(
                        ServerMainListenerResponseGenerator.nameTaken()
                );
                return;
            }
        }
        connection.setName(name);
        connection.write(
                ServerMainListenerResponseGenerator.nameApproved()
        );
    }

    public static void handleConnectionLeftRoom(Connection connection) {
        Server server = ServerSingleton.getServer();
        try {
            Room room = server.getRoomByConnection(connection);
            room.removeConnection(connection);
            room.sendMessageToAll(
                    ServerMainListenerResponseGenerator.connectionLeftRoom(connection.getName())
            );
        } catch (NoRoomFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
