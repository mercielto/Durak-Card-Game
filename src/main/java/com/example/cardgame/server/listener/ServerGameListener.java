package com.example.cardgame.server.listener;

import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.GameCommands;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.exception.PlayerNotFoundException;
import com.example.cardgame.server.game.DurakGame;
import com.example.cardgame.server.game.Player;
import com.example.cardgame.server.service.ServerGameService;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerGameListener extends Thread {
    private Room room;
    private Connection connection;

    public ServerGameListener(Connection connection1, Room room1) {
        room = room1;
        connection = connection1;
    }

    @Override
    public void run() {
        BufferedReader reader = connection.getReader();
        DurakGame game = room.getGame();
        Player player = null;
        try {
            player = game.getPlayer(connection);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                String message = reader.readLine();
                if (!isInterrupted()){
                    if (message != null) {
                        String[] splitMessage = message.split(ServerProperties.getMainDelimiter());
                        GameCommands command = GameCommands.defineCommand(splitMessage[0]);
                        System.out.println("GameListener received: " + command);

                        switch (command) {
                            case NEW_CARD_ON_TABLE ->
                                    ServerGameService.handleNewCardOnTable(splitMessage, player, room);
                            case BEAT_CARD -> ServerGameService.handleBeatCard(game, player, splitMessage);
                            case END_MOVE -> ServerGameService.handleEndMove(game, player);
                            case TAKE_CARDS -> ServerGameService.handleTakeCards(game, player);
                            case NO_MORE_CARDS_ON_HANDS -> ServerGameService.handleNoMoreCardsOnHands(game, player);
                            case FORCED_END_OF_MOVE -> ServerGameService.handleForcedEndOfMove(game);
                            case QUIT_GAME -> ServerGameService.handleQuit(game, player);
                            default -> System.out.println("GameListener: Unknown command --- %s".formatted(command));
                        }
                    }
                } else {
                    System.out.println("GameListener: stopping");
                    break;
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}