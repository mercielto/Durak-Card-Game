package com.example.cardgame.server.listener;

import com.example.cardgame.server.ServerSingleton;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Server;
import com.example.cardgame.server.service.ServerMainService;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerMainListener extends Thread {
    private Connection connection;
    private final Server server = ServerSingleton.getServer();

    public ServerMainListener(Connection c) {
        connection = c;
    }

    @Override
    public void run() {
        System.out.println("MainListener: starting...");
        BufferedReader reader = connection.getReader();
        boolean handleMessages = true;
        while (handleMessages) {
            try {
                if (isInterrupted()) {
                    break;
                }
                if (reader.ready() && !isInterrupted()) {
                    String message = reader.readLine();
                    if (!isInterrupted()) {
                        if (message != null) {
                            String[] splitMessage = message.split(ServerProperties.getMainDelimiter());
                            MenuCommands command = MenuCommands.defineCommand(splitMessage[0]);
                            System.out.println("MainListener received: " + command);

                            switch (command) {
                                case JOIN_ROOM -> ServerMainService.handleJoinRoom(connection, splitMessage);
                                case LEAVE_ROOM -> ServerMainService.handleLeaveRoom(connection);
                                case CREATE_ROOM -> ServerMainService.handleRoomCreation(connection, splitMessage);
                                case START_GAME -> handleMessages = false;
                                case GET_AVAILABLE_ROOMS -> ServerMainService.handleGetAvailableRooms(connection, splitMessage);
                                case GET_PLAYERS_LIST_VIEW -> ServerMainService.handleGetPlayersListView(connection, splitMessage);
                                case READY -> ServerMainService.handleReady(connection);
                                case SET_NAME -> ServerMainService.setName(connection, splitMessage[1]);
                                case CONNECTION_LEFT_ROOM -> ServerMainService.handleConnectionLeftRoom(connection);
                            }
                        }
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
