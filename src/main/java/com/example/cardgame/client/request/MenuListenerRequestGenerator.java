package com.example.cardgame.client.request;

import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.properties.commands.MenuCommands;

import java.util.StringJoiner;


public class MenuListenerRequestGenerator {
    public static String getAvailableGames(SearchTypeProperty property, String searchParameter) {
        StringJoiner joiner = new StringJoiner(ServerProperties.getMainDelimiter());
        joiner.add(String.valueOf(MenuCommands.GET_AVAILABLE_ROOMS.getValue()));

        if (searchParameter.length() != 0) {
            joiner.add(property.getRequestValue());
            joiner.add(searchParameter);
        }

        return joiner.toString();
    }
}
