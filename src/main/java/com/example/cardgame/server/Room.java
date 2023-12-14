package com.example.cardgame.server;

import com.example.cardgame.server.game.DurakGame;
import com.example.cardgame.server.game.Player;
import com.example.cardgame.server.listener.ServerGameListener;

import java.util.*;

public class Room {
    private final List<Connection> connections = new ArrayList<>();
    private Set<Connection> readyPlayers = new HashSet<>();

    private final UUID uuid = UUID.randomUUID();
    private int customMaxPlayersSize = DurakGame.maxPlayersSize;
    private int customMinPlayersSize = DurakGame.minPlayersSize;
    private String roomName = String.valueOf(uuid);

    private DurakGame game;

    public boolean setCustomMaxConnectionsSize(int max) {
        if (max <= DurakGame.maxPlayersSize) {
            customMaxPlayersSize = max;
            return true;
        }
        return false;
    }

    public void setRoomName(String name) {
        roomName = name;
    }

    public boolean setCustomMinConnectionsSize(int min) {
        if (min >= DurakGame.minPlayersSize) {
            customMinPlayersSize = min;
            return true;
        }
        return false;
    }

    public boolean addConnection(Connection connection) {
        if (connections.size() < customMaxPlayersSize) {
            connections.add(connection);
            return true;
        }
        return false;
    }

    public boolean isOpen() {
        return connections.size() < customMaxPlayersSize;
    }

    public void sendMessageToAll(String text) {
        for (Connection connection : connections) {
            connection.write(text);
        }
    }

    public void sendMessageToAll(String text, List<Connection> except) {
        List<Connection> others = new ArrayList<>(List.copyOf(connections));
        others.removeAll(except);
        for (Connection connection : others) {
            connection.write(text);
        }
    }

    public void startGame() {
        if (game == null) {
            game = new DurakGame(this);
            for (Connection connection : connections) {
                connection.setListener(new ServerGameListener(connection, this));
            }
        } else {
            sendMessageToAll("Error");   // write error message
        }
    }

    public String getRoomName() {
        return roomName;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean contains(Connection connection) {
        for (Connection pConnection : connections) {
            if (pConnection.equals(connection)) {
                return true;
            }
        }
        return false;
    }

    public void removeConnection(Connection connection) {
        connections.remove(connection);
    }

    public int getPlayersCount() {
        return connections.size();
    }

    public int getCustomMaxPlayersSize() {
        return customMaxPlayersSize;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public boolean isFull() {
        return connections.size() == customMaxPlayersSize;
    }

    public void setReadyPlayer(Connection connection) {
        if (connections.contains(connection)) {
            readyPlayers.add(connection);
        }
    }

    public Set<Connection> getReadyPlayers() {
        return readyPlayers;
    }

    public void clearReadyPlayers() {
        readyPlayers.clear();
    }

    public boolean isGameIsOn() {
        return game != null;
    }

    public boolean isEveryoneReady() {
        return readyPlayers.size() == connections.size();
    }

    public DurakGame getGame() {
        return game;
    }

    public void sendMessageToConnectionsInGame(String text) {
        if (game != null) {
            List<Player> players = game.getPlayers();
            for (Player player : players) {
                player.getConnection().write(text);
            }
        }
    }

    public void sendMessageToConnectionsInGame(String text, List<Player> except) {
        if (game != null) {
            List<Player> players = new ArrayList<>(game.getPlayers());
            players.removeAll(except);
            for (Player player : players) {
                player.getConnection().write(text);
            }
        }
    }
}
