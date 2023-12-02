package com.example.cardgame.server;

import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.server.exception.NoRoomFoundException;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.server.listener.MainListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Server {
    private List<Connection> connections = new ArrayList<>();
    public List<Room> rooms = new ArrayList<>();            /// should be private

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(ServerProperties.getPort())) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Someone just connected to the server");

                Connection connection = new Connection(socket);
                connections.add(connection);

                MainListener mainListener = new MainListener(connection, this);
                connection.setListener(mainListener);
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
        throw new NoRoomFoundException(uuid);
    }

    public void addConnectionToRoom(Connection connection, UUID uuid) throws NoRoomFoundException {
        getRoomByUuid(uuid).addConnection(connection);
    }

    public void leaveRoom(Connection connection) {
        for (Room room : rooms) {
            if (room.contains(connection)) {
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

    public void createRoom(Connection connection) {
        Room room = new Room();
        room.addConnection(connection);
        rooms.add(room);
    }
}