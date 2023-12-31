package com.example.cardgame.client.listener;

import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.service.MenuHandlerService;
import com.example.cardgame.gameProperties.exception.CommandNotDefinedException;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class ClientMenuListener extends Thread {
    @Override
    public void run() {
        BufferedReader reader = ClientSingleton.getClient().getReader();
        System.out.println("MenuListener: starting...");
        while (!isInterrupted()) {
            try {
                if (reader.ready() && !isInterrupted()) {
                    String message = reader.readLine();
                    System.out.println("MenuListener: Received " + message);
                    if (!isInterrupted()) {
                        String[] split = message.split(ServerProperties.getMainDelimiter());
                        MenuCommands command = MenuCommands.defineCommand(split[0]);

                        Platform.runLater(() -> {
                            switch (command) {
                                case SOMEONE_JOINED_ROOM -> MenuHandlerService.addPlayerToListView(split[1]);
                                case GET_AVAILABLE_ROOMS -> MenuHandlerService.getAvailableRooms(message);
                                case CREATE_ROOM -> MenuHandlerService.createRoom(split[1]);
                                case GET_PLAYERS_LIST_VIEW -> MenuHandlerService.getPlayersListView(split);
                                case WAITING_FOR_CONFIRMATION -> MenuHandlerService.waitingForConfirmation();
                                case LEAVE_ROOM -> MenuHandlerService.leaveRoom();
                                case START_GAME -> MenuHandlerService.startGame(List.of(split));
                                case CONNECTION_LEFT_ROOM -> MenuHandlerService.handleConnectionLeftRoom(split);
                                case ERROR -> System.out.println("Что-то какая-то ошибка...");
                                case NAME_TAKEN -> MenuHandlerService.handleNameTaken();
                                case NAME_APPROVED -> MenuHandlerService.handleNameApproved();
                            }
                        });
                    }
                }
            } catch (CommandNotDefinedException e) {
                System.err.println(e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("MenuListener: stopping...");
    }
}
