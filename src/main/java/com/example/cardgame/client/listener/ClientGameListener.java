package com.example.cardgame.client.listener;

import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.service.GameHandlerService;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.GameCommands;
import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientGameListener extends Thread {
    @Override
    public void run() {
        System.out.println("GameListener is on!!!");
        BufferedReader reader = ClientSingleton.getClient().getReader();
        while (true) {
            try {
                if (interrupted()) {
                    break;
                }

                String message = reader.readLine();
                System.out.println("GameListener: Received " + message);
                if (!isInterrupted()) {
                    String[] split = message.split(ServerProperties.getMainDelimiter());
                    GameCommands command = GameCommands.defineCommand(split[0]);

                    Platform.runLater(() -> {
                        switch (command) {
                            case YOUR_MOVE -> GameHandlerService.handleYourMove();
                            case NOT_YOUR_MOVE_YET -> GameHandlerService.handleNotYourMove();
                            case NEW_CARD_ON_TABLE -> GameHandlerService.handleNewCardOnTable(split);
                        }
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

