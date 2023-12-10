package com.example.cardgame.client.listener;

import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.service.MenuHandlerService;
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
        while (!isInterrupted()) {
            try {
                if (interrupted()) {
                    break;
                }
                if (reader.ready() && !interrupted()) {
                    String message = reader.readLine();
                    System.out.println("MenuListener: Received " + message);
                    if (!isInterrupted()) {
                        String[] split = message.split(ServerProperties.getMainDelimiter());
                        MenuCommands command = MenuCommands.defineCommand(split[0]);

                        Platform.runLater(() -> {
                            switch (command) {
                                case SOMEONE_JOINED_ROOM -> MenuHandlerService.updateListView(split[1]);
                                case GET_AVAILABLE_ROOMS -> MenuHandlerService.getAvailableRooms(message);
                                case CREATE_ROOM -> MenuHandlerService.createRoom(split[1]);
                                case GET_PLAYERS_LIST_VIEW -> MenuHandlerService.getPlayersListView(split);
                                case WAITING_FOR_CONFIRMATION -> MenuHandlerService.waitingForConfirmation();
                                case LEAVE_ROOM -> MenuHandlerService.leaveRoom();
                                case START_GAME -> MenuHandlerService.startGame(List.of(split));
                                case ERROR -> System.out.println("Что-то какая-то ошибка...");
                            }
                        });
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
