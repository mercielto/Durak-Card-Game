package com.example.cardgame.client.service;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.client.request.MenuListenerRequestGenerator;
import com.example.cardgame.client.response.model.RoomResponse;
import com.example.cardgame.client.response.parser.MenuResponseParser;

import java.util.ArrayList;
import java.util.List;

public class MenuHandlerService {
    public static List<String> getAvailableRooms(SearchTypeProperty property, String searchProperty) {
        Client client = ClientSingleton.getClient();
        client.write(MenuListenerRequestGenerator.getAvailableGames(property, searchProperty));
        String message = client.read();
        List<RoomResponse> roomResponses = MenuResponseParser.parseGetAvailableRooms(message);

        List<String> ans = new ArrayList<>();
        for (RoomResponse response : roomResponses) {
            ans.add(response.toString());
        }
        return ans;
    }
}
