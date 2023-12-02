package com.example.cardgame.server;

public class ServerSingleton {
    private static Server server;

    public static Server getServer() {
        if (server == null) {
            server = new Server();
        }
        return server;
    }
}
