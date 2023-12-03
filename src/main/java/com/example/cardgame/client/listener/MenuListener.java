package com.example.cardgame.client.listener;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.service.MenuHandlerService;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

public class MenuListener extends Thread {
    private Client client;

    @Override
    public void run() {
        BufferedReader reader = client.getReader();
        while (true) {
            try {
                String message = reader.readLine();
                String[] splitted = message.split(ServerProperties.getMainDelimiter());
                MenuCommands command = MenuCommands.defineCommand(splitted[0]);

                switch (command) {
                    case SOMEONE_JOINED_ROOM -> {
                        MenuHandlerService.updateListView(splitted[1]);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
