package com.example.cardgame;

import com.example.cardgame.server.Server;
import com.example.cardgame.server.ServerSingleton;

public class Main {
    public static void main(String[] args) {
        Server server = ServerSingleton.getServer();
        server.start();
    }
}
