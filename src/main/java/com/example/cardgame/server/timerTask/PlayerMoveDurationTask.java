package com.example.cardgame.server.timerTask;

import com.example.cardgame.server.game.DurakGame;
import com.example.cardgame.server.game.Player;

import java.util.TimerTask;


public class PlayerMoveDurationTask extends TimerTask {
    private DurakGame durakGame;
    private Player player;

    public PlayerMoveDurationTask(DurakGame dg, Player player1) {
        durakGame = dg;
        player = player1;
    }

    @Override
    public void run() {
        durakGame.removePlayer(player);
    }
}
