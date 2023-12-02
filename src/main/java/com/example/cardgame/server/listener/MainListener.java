package com.example.cardgame.server.listener;

import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.server.exception.NoRoomFoundException;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.Server;
import com.example.cardgame.server.handler.MainListenerHandler;
import com.example.cardgame.server.responseGenerator.MainListenerResponseGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class MainListener extends Thread {
    private Connection connection;
    private Server server;

    public MainListener(Connection c, Server s) {
        connection = c;
        server = s;
    }

    @Override
    public void run() {
        BufferedReader reader = connection.getReader();
        boolean handleMessages = true;
        while (handleMessages) {
            try {
                String message = reader.readLine();
                if (message != null) {
                    String[] splitMessage = message.split(ServerProperties.getMainDelimiter());
                    MenuCommands command = MenuCommands.defineCommand(Integer.parseInt(splitMessage[0]));

                    switch (command) {
                        case JOIN_ROOM -> {
                            UUID uuid = UUID.fromString(splitMessage[1]);
                            server.addConnectionToRoom(connection, uuid);
                        }
                        case LEAVE_ROOM -> server.leaveRoom(connection);
                        case CREATE_ROOM -> server.createRoom(connection);
                        case START_GAME -> handleMessages = false;
                        case GET_AVAILABLE_ROOMS -> {
                            List<Room> rooms = MainListenerHandler.getAvailableRooms(splitMessage);
                            connection.write(
                                    MainListenerResponseGenerator.getAvailableRooms(rooms)
                            );
                        }
                    }
                }

            } catch (IOException | NoRoomFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
