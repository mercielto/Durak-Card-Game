package com.example.cardgame.server.listener;

import com.example.cardgame.server.exception.NoRoomFoundException;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.Server;
import com.example.cardgame.server.service.ServerMainService;
import com.example.cardgame.server.model.request.RoomRequest;
import com.example.cardgame.server.responseGenerator.ServerMainListenerResponseGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ServerMainListener extends Thread {
    private Connection connection;
    private Server server;

    public ServerMainListener(Connection c, Server s) {
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
                    MenuCommands command = MenuCommands.defineCommand(splitMessage[0]);

                    switch (command) {
                        case JOIN_ROOM ->
                                ServerMainService.handleJoinRoom(connection, splitMessage);
                        case LEAVE_ROOM ->
                                ServerMainService.handleLeaveRoom(connection);
                        case CREATE_ROOM ->
                                ServerMainService.handleRoomCreation(connection, splitMessage);
                        case START_GAME ->
                                handleMessages = false;
                        case GET_AVAILABLE_ROOMS ->
                                ServerMainService.handleGetAvailableRooms(connection ,splitMessage);
                        case GET_PLAYERS_LIST_VIEW ->
                                ServerMainService.handleGetPlayersListView(connection, splitMessage);
                        case READY ->
                            ServerMainService.handleReady(connection);
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
