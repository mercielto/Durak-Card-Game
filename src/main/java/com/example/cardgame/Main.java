package com.example.cardgame;

import com.example.cardgame.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
