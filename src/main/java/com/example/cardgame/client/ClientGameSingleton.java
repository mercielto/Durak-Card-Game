package com.example.cardgame.client;

import com.example.cardgame.client.game.ClientGame;

public class ClientGameSingleton {
    private static ClientGame game;

    public static ClientGame getGame() {
        if (game == null) {
            game = new ClientGame();
        }
        return game;
    }

    public static void clear() {
        game = null;
    }
}
