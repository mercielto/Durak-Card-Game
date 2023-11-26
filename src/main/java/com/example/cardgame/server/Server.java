package com.example.cardgame.server;

import com.example.cardgame.server.listener.MainListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public List<Connection> connections = new ArrayList<>();
    public List<Room> rooms = new ArrayList<>();

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(8889)) {
            while (true) {
                Socket socket = serverSocket.accept();

                Connection connection = new Connection(socket);
                connections.add(connection);

                MainListener mainListener = new MainListener(connection, this);
                connection.setListener(mainListener);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}