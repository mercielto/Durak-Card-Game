package com.example.cardgame.server.handler;

import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.server.Room;
import com.example.cardgame.server.ServerSingleton;

import java.util.List;

public class MainListenerHandler {
    public static List<Room> getAvailableRooms(String[] split) {
        SearchTypeProperty typeProperty;
        String value;
        if (split.length == 1) {
            typeProperty = SearchTypeProperty.GET_ALL;
            value = "";
        } else {
            typeProperty = SearchTypeProperty.defineByRequestValue(split[1]);
            value = split[2];
        }
        return ServerSingleton.getServer().getAvailableRooms(typeProperty, value);
    }
}
