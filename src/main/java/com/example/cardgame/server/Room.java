package com.example.cardgame.server;

import com.example.cardgame.game.DurakGame;
import com.example.cardgame.server.listener.RoomListener;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final List<Connection> players = new ArrayList<>();

    private int customMaxPlayersSize = DurakGame.maxPlayersSize;
    private int customMinPlayersSize = DurakGame.minPlayersSize;

    private DurakGame durakGame;

    public boolean setCustomMaxConnectionsSize(int max) {
        if (max <= DurakGame.maxPlayersSize) {
            customMaxPlayersSize = max;
            return true;
        }
        return false;
    }

    public boolean setCustomMinConnectionsSize(int min) {
        if (min >= DurakGame.minPlayersSize) {
            customMinPlayersSize = min;
            return true;
        }
        return false;
    }

    public boolean addConnection(Connection connection) {
        if (players.size() < customMaxPlayersSize) {
            connection.setListener(new RoomListener(connection, this));
            players.add(connection);
            return true;
        }
        return false;
    }

    public boolean isOpen() {
        return players.size() < customMaxPlayersSize;
    }

    public boolean canStart() {
        return players.size() >= customMinPlayersSize;
    }

    public void sendMessageToAll(String text) {
        for (Connection connection : players) {
            connection.write(text);
        }
    }

    public void start() {
        if (durakGame == null) {
            durakGame = new DurakGame(players);
        } else {
            sendMessageToAll("");   // write error message
        }
    }
}
