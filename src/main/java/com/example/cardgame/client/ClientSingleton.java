package com.example.cardgame.client;

public class ClientSingleton {
    private static Client client;

    public static Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }
}
