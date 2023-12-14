package com.example.cardgame.server;

import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.properties.commands.MenuCommands;
import com.example.cardgame.server.exception.NoRoomFoundException;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.server.listener.ServerMainListener;
import com.example.cardgame.server.model.request.RoomRequest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server {
    private List<Connection> connections = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(ServerProperties.getPort())) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Someone just connected to the server");

                Connection connection = new Connection(socket);
                connections.add(connection);

                ServerMainListener serverMainListener = new ServerMainListener(connection, this);
                connection.setListener(serverMainListener);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public Room getRoomByUuid(UUID uuid) throws NoRoomFoundException {
        for (Room room : rooms) {
            if (room.getUuid().equals(uuid)) {
                return room;
            }
        }
        throw new NoRoomFoundException(uuid.toString());
    }

    public void addConnectionToRoom(Connection connection, UUID uuid) throws NoRoomFoundException {
        getRoomByUuid(uuid).addConnection(connection);
    }

    public void leaveRoom(Connection connection, UUID uuid) {
        for (Room room : rooms) {
            if (room.getUuid().equals(uuid)) {
                room.removeConnection(connection);
                return;
            }
        }
    }


    public List<Room> getAvailableRooms(SearchTypeProperty property, String searchParam) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : rooms) {
            if (room.isOpen()) {
                switch (property) {
                    case ROOM_ID -> {
                        if (room.getUuid() == UUID.fromString(searchParam)) {
                            availableRooms.add(room);
                        }
                    }
                    case ROOM_NAME -> {
                        if (room.getRoomName().matches("(.)*%s(.)*".formatted(searchParam))) {
                            availableRooms.add(room);
                        }
                    }
                    case GET_ALL -> availableRooms.add(room);
                }

            }
        }
        return availableRooms;
    }

    public boolean isConnectionHasRoom(Connection connection) {
        for (Room room : rooms) {
            if (room.contains(connection)) {
                return true;
            }
        }
        return false;
    }

    public String createRoom(Connection connection, RoomRequest request) {
        if (isConnectionHasRoom(connection)) {
            return String.valueOf(MenuCommands.ERROR.getValue());
        }
        Room room = new Room();
        if (request.getName() == null) {
            room.setRoomName(room.getUuid().toString());
        } else {
            room.setRoomName(request.getName());
        }
        room.setCustomMaxConnectionsSize(request.getMaxPlayersCount());
        room.addConnection(connection);
        rooms.add(room);
        return room.getUuid().toString();
    }

    public Room getRoomByConnection(Connection connection) throws NoRoomFoundException {
        for (Room room : rooms) {
            if (room.contains(connection)) {
                return room;
            }
        }
        throw new NoRoomFoundException(connection.getName());
    }
}