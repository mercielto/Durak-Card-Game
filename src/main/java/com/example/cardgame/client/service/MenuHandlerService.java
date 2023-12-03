package com.example.cardgame.client.service;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.properties.SearchTypeProperty;
import com.example.cardgame.client.request.MenuListenerRequestGenerator;
import com.example.cardgame.client.response.model.RoomResponse;
import com.example.cardgame.client.response.server_parser.MenuResponseParser;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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

    public static String createRoom(String roomName, Integer maxPlayerCount) {
        Client client = ClientSingleton.getClient();
        client.write(MenuListenerRequestGenerator.createRoom(roomName, maxPlayerCount));
        return client.read();
    }

    public static String getPlayerInfo() {
        Client client = ClientSingleton.getClient();
        return client.getName();
    }

    public static void joinRoom(RoomResponse response) {
        Client client = ClientSingleton.getClient();
        client.write(MenuListenerRequestGenerator.joinRoom(response.getUuid().toString()));
    }

    // CHECK FIRSTLY
    public static void updateListView(String name) {
        Stage stage = StageSingleton.getStage();
        Scene scene = stage.getScene();
        ListView<String> listView = (ListView<String>) scene.lookup("#listView");
        List<String> lv = listView.getItems();
        lv.add(name);
        listView.setItems((ObservableList<String>) lv);
        stage.show();
    }
}
