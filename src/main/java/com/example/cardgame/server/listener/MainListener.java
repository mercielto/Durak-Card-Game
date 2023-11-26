package com.example.cardgame.server.listener;

import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Server;

import java.io.BufferedReader;
import java.io.IOException;

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
        while (!isInterrupted()) {
            try {
                String message = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
