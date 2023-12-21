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
        System.out.println("GameListener: starting...");
        BufferedReader reader = ClientSingleton.getClient().getReader();
        while (!isInterrupted()) {
            try {
                if (reader.ready() && !isInterrupted()) {
                    String message = reader.readLine();
                    String[] split = message.split(ServerProperties.getMainDelimiter());
                    GameCommands command = GameCommands.defineCommand(split[0]);
                    System.out.println("GameListener: Received " + command);

                    Platform.runLater(() -> {
                        switch (command) {
                            case YOUR_MOVE -> GameHandlerService.handleYourMove();
                            case NOT_YOUR_MOVE_YET -> GameHandlerService.handleNotYourMove();
                            case NEW_CARD_ON_TABLE -> GameHandlerService.handleNewCardOnTable(split);
                            case SET_PLAYERS_ORDER -> GameHandlerService.setPlayersOrder(split[1]);
                            case BEAT_CARD -> GameHandlerService.handleBeatCard(split);
                            case ADD_NEW_CARDS_ON_HANDS -> GameHandlerService.handleAddNewCardsOnHands(split);
                            case END_MOVE -> GameHandlerService.handleEndMove();
                            case ADD_NEW_CARDS_TO_USER -> GameHandlerService.handleNewCardToUser(split);
                            case TAKE_CARDS -> GameHandlerService.handleTakeCards();
                            case PLAYER_WON_THE_GAME -> GameHandlerService.handlePlayerWonTheGame(split);
                            case QUIT_GAME_NOTIFICATION -> GameHandlerService.handleQuitGame(split);
                            case FOOL -> GameHandlerService.handleFool(split);
                            case DRAW -> GameHandlerService.handleDraw();
                        }
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("GameListener: stopping...");
    }
}

