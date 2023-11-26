package com.example.cardgame.server.listener;

import com.example.cardgame.game.DurakGame;
import com.example.cardgame.server.Connection;

public class GameListener extends Thread {
    private DurakGame game;
    private Connection connection; // may be player?

    public GameListener(Connection connection1, DurakGame durakGame) {
        game = durakGame;
        connection = connection1;
    }

    @Override
    public void run() {
        super.run();
    }
}
