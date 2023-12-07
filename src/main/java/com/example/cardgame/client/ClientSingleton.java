package com.example.cardgame.client;

import com.example.cardgame.client.listener.ClientMenuListener;

import java.util.UUID;

public class ClientSingleton {
    private static Client client;

    public static Client getClient() {
        if (client == null) {
            client = new Client(UUID.randomUUID().toString().substring(1, 5));
            client.setInputListener(new ClientMenuListener());
        }
        return client;
    }
}
