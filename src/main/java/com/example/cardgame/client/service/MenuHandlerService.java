package com.example.cardgame.client.service;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.client.application.MainApplication;
import com.example.cardgame.properties.FxmlObjectProperties;
import com.example.cardgame.client.request.generator.ClientMenuListenerRequestGenerator;
import com.example.cardgame.client.response.model.RoomResponse;
import com.example.cardgame.client.response.server_parser.MenuResponseParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuHandlerService {
    public static void getAvailableRooms(String message) {
        List<RoomResponse> roomResponses = MenuResponseParser.parseGetAvailableRooms(message);

        List<String> rooms = new ArrayList<>();
        for (RoomResponse response : roomResponses) {
            rooms.add(response.toString());
        }

        ObservableList<String> roomsObservableList = FXCollections.observableArrayList(rooms);
        FxmlObjectsGetter.getListViewById(FxmlObjectProperties.menuListViewId)
                .setItems(roomsObservableList);
    }

    public static void createRoom(String id) {
        ListView<String> listView = FxmlObjectsGetter.getListViewById(FxmlObjectProperties.roomListViewId);
        TextField roomName = FxmlObjectsGetter.getTextFieldById(FxmlObjectProperties.roomNameId);
        TextField roomId = FxmlObjectsGetter.getTextFieldById(FxmlObjectProperties.roomIdId);
        ChoiceBox<String> maxPlayersCount = FxmlObjectsGetter.getChoiceBoxById(FxmlObjectProperties.roomChoiceBoxId);
        Button createBtn = FxmlObjectsGetter.getButtonById(FxmlObjectProperties.roomCreateBtnId);

        String player = MenuHandlerService.getPlayerInfo();
        ObservableList<String> roomsObservableList = FXCollections.observableArrayList(player);
        listView.setItems(roomsObservableList);

        if (roomName.getText().length() == 0) {
            roomName.setText(id);
        }
        roomId.setText(id);
        roomId.setEditable(false);
        maxPlayersCount.setDisable(true);
        createBtn.setDisable(true);
        roomName.setDisable(true);
    }

    public static String getPlayerInfo() {
        Client client = ClientSingleton.getClient();
        return client.getName();
    }

    public static void joinRoom(RoomResponse response) {
        Client client = ClientSingleton.getClient();
        client.write(ClientMenuListenerRequestGenerator.joinRoom(response.getUuid().toString()));
    }

    public static void updateListView(String name) {
        ListView<String> listView = FxmlObjectsGetter.getListViewById(FxmlObjectProperties.roomListViewId);
        List<String> list = listView.getItems();

        list.add(name);
        listView.setItems(FXCollections.observableList(list));
    }

    public static void sendRequestForListView(UUID uuid) {
         Client client = ClientSingleton.getClient();
         client.write(ClientMenuListenerRequestGenerator.getPlayersListView(uuid.toString()));
    }

    public static void getPlayersListView(String[] names) {
        List<String> namesList = List.of(names);
        namesList = namesList.subList(1, namesList.size());
        ObservableList<String> roomsObservableList = FXCollections.observableArrayList(namesList);

        ListView<String> listView = FxmlObjectsGetter.getListViewById(FxmlObjectProperties.roomListViewId);
        listView.setItems(roomsObservableList);
    }

    public static void waitingForConfirmation() {
        Button button = FxmlObjectsGetter.getButtonById(FxmlObjectProperties.readyBtnId);
        button.opacityProperty().setValue(1);
        button.setDisable(false);
    }

    public static void leaveRoom() {
        try {
            (new MainApplication()).start(StageSingleton.getStage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
