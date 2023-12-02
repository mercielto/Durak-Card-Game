package com.example.cardgame.server.responseGenerator;

import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;
import com.example.cardgame.server.Room;

import java.util.List;
import java.util.StringJoiner;

public class MainListenerResponseGenerator {
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
}
