package com.example.cardgame.client;

import com.example.cardgame.client.listener.ClientMenuListener;
import com.example.cardgame.client.request.generator.ClientMenuRequestGenerator;

import java.util.UUID;

public class ClientSingleton {
    private static Client client;

    public static Client getClient() {
        if (client == null) {
            client = new Client();
            client.setName(UUID.randomUUID().toString().substring(1, 5));
            client.write(
                    ClientMenuRequestGenerator.setName(client.getName())
            );
            System.out.println("Client with name " + client.getName() + " created");
            client.setInputListener(new ClientMenuListener());
        }
        return client;
    }
}
