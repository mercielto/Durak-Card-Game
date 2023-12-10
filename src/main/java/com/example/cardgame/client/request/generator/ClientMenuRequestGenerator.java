package com.example.cardgame.client.request.generator;

import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;

import java.util.StringJoiner;


public class ClientMenuRequestGenerator {
    public static String getAvailableRooms(SearchTypeProperty property, String searchParameter) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(String.valueOf(MenuCommands.GET_AVAILABLE_ROOMS.getValue()));

        if (searchParameter.length() != 0) {
            joiner.add(property.getRequestValue());
            joiner.add(searchParameter);
        }

        return joiner.toString();
    }

    public static String createRoom(String roomName, Integer maxPlayerCount) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(String.valueOf(MenuCommands.CREATE_ROOM.getValue()));
        joiner.add(String.valueOf(maxPlayerCount));
        if (roomName.length() != 0) {
            joiner.add(roomName);
        }
        return joiner.toString();
    }

    public static String joinRoom(String id) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(MenuCommands.JOIN_ROOM.getValue());
        joiner.add(id);
        return joiner.toString();
    }

    public static String getPlayersListView(String string) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(MenuCommands.GET_PLAYERS_LIST_VIEW.getValue());
        joiner.add(string);
        return joiner.toString();
    }

    public static String leaveRoom() {
        return MenuCommands.LEAVE_ROOM.getValue();
    }

    public static String ready() {
        return MenuCommands.READY.getValue();
    }
}
