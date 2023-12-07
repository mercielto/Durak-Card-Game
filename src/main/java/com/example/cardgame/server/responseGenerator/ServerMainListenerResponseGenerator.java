package com.example.cardgame.server.responseGenerator;

import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;
import com.example.cardgame.server.Connection;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.ServerSingleton;
import com.example.cardgame.server.exception.NoRoomFoundException;

import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class ServerMainListenerResponseGenerator {
    public static String getAvailableRooms(List<Room> rooms) {
        StringJoiner response = new StringJoiner(ServerProperties.getMainDelimiter());
        response.add(String.valueOf(MenuCommands.GET_AVAILABLE_ROOMS.getValue()));
        for (Room room : rooms) {
            StringJoiner partialJoiner = new StringJoiner(ServerProperties.getSideDelimiter());
            partialJoiner.add(room.getUuid().toString());
            partialJoiner.add(room.getRoomName());
            partialJoiner.add(String.valueOf(room.getPlayersCount()));
            partialJoiner.add(String.valueOf(room.getCustomMaxPlayersSize()));

            response.add(partialJoiner.toString());
        }
        return response.toString();
    }

    public static String getRoomPlayersNames(String id) throws NoRoomFoundException {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(MenuCommands.GET_PLAYERS_LIST_VIEW.getValue());
        Room room = ServerSingleton.getServer().getRoomByUuid(UUID.fromString(id));
        for (Connection connection : room.getPlayers()) {
            joiner.add(connection.getName());
        }
        return joiner.toString();
    }

    public static String someoneJoinedRoom(String playerName) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(MenuCommands.SOMEONE_JOINED_ROOM.getValue());
        joiner.add(playerName);
        return joiner.toString();
    }

    public static String createRoom(String uuid) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(MenuCommands.CREATE_ROOM.getValue());
        joiner.add(uuid);
        return joiner.toString();
    }

    public static String waitingForConfirmationToStartGame() {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(MenuCommands.WAITING_FOR_CONFIRMATION.getValue());
        return joiner.toString();
    }

    public static String leaveRoom() {
        return MenuCommands.LEAVE_ROOM.getValue();
    }

    public static String startGame() {
        return MenuCommands.START_GAME.getValue();
    }
}
