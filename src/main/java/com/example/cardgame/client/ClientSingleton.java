package com.example.cardgame.client;

import com.example.cardgame.client.listener.ClientMenuListener;
import com.example.cardgame.client.request.generator.ClientMenuRequestGenerator;

import java.util.UUID;

public class ClientSingleton {
    private static Client client;

    public static Client getClient() {
        if (client == null) {
            client = new Client();
            client.setInputListener(new ClientMenuListener());
        }
        return client;
    }
}
