package com.example.cardgame.client.service;

import com.example.cardgame.client.*;
import com.example.cardgame.client.application.GameApplication;
import com.example.cardgame.client.application.MainApplication;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.client.request.generator.ClientMenuRequestGenerator;
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
        FxmlObjectsGetter.getListViewById(FxmlObjectProperties.LIST_VIEW_ROOMS)
                .setItems(roomsObservableList);
    }

    public static void createRoom(String id) {
        ListView<String> listView = FxmlObjectsGetter.getListViewById(FxmlObjectProperties.ROOM_LIST_VIEW_ID);
        TextField roomName = FxmlObjectsGetter.getTextFieldById(FxmlObjectProperties.ROOM_NAME);
        TextField roomId = FxmlObjectsGetter.getTextFieldById(FxmlObjectProperties.ROOM_ID);
        ChoiceBox<String> maxPlayersCount = FxmlObjectsGetter.getChoiceBoxById(FxmlObjectProperties.MAX_PLAYERS_COUNT);
        Button createBtn = FxmlObjectsGetter.getButtonById(FxmlObjectProperties.CREATE_BTN);

        String player = MenuHandlerService.getPlayerInfo();
        ObservableList<String> roomsObservableList = FXCollections.observableArrayList(player);
        listView.setItems(roomsObservableList);

        RoomResponse roomResponse = RoomResponse.builder()
                .uuid(UUID.fromString(id))
                .players(new ArrayList<>(List.of(player)))
                .maxPlayersCount(Integer.parseInt(maxPlayersCount.getValue()))
                .build();

        if (roomName.getText().length() == 0) {
            roomResponse.setName(id);
            roomName.setText(id);
        }
        roomId.setText(id);
        roomId.setEditable(false);
        maxPlayersCount.setDisable(true);
        createBtn.setDisable(true);
        roomName.setEditable(false);

        ClientRoomSingleton.setRoom(roomResponse);
    }

    public static String getPlayerInfo() {
        Client client = ClientSingleton.getClient();
        return client.getName();
    }

    public static void joinRoom(RoomResponse response) {
        Client client = ClientSingleton.getClient();
        client.write(ClientMenuRequestGenerator.joinRoom(response.getUuid().toString()));
    }

    public static void addPlayerToListView(String name) {
        ClientRoomSingleton.getRoom().addPlayer(name);
        updateListView();
    }

    private static void updateListView() {
        ListView<String> listView = FxmlObjectsGetter.getListViewById(FxmlObjectProperties.ROOM_LIST_VIEW_ID);
        listView.setItems(FXCollections.observableList(ClientRoomSingleton.getRoom().getPlayers()));
    }

    public static void sendRequestForListView(UUID uuid) {
         Client client = ClientSingleton.getClient();
         client.write(ClientMenuRequestGenerator.getPlayersListView(uuid.toString()));
    }

    public static void getPlayersListView(String[] names) {
        List<String> namesList = List.of(names);
        namesList = namesList.subList(1, namesList.size());
        ObservableList<String> roomsObservableList = FXCollections.observableArrayList(namesList);

        ListView<String> listView = FxmlObjectsGetter.getListViewById(FxmlObjectProperties.ROOM_LIST_VIEW_ID);
        listView.setItems(roomsObservableList);

        ClientRoomSingleton.getRoom().setPlayers(namesList);
    }

    public static void waitingForConfirmation() {
        Button button = FxmlObjectsGetter.getButtonById(FxmlObjectProperties.READY_BTN);
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

    public static void startGame(List<String> split) {
        Card trumpCard = Card.getCard(split.get(1));
        List<Card> handCards = new ArrayList<>();
        for (String stringCard : split.subList(2, split.size())) {
            handCards.add(Card.getCard(stringCard));
        }

        try {
            (new GameApplication()).start(StageSingleton.getStage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        GameHandlerService.setCardsOnGameStart(trumpCard, handCards);
    }

    public static void handleConnectionLeftRoom(String[] split) {
        RoomResponse room = ClientRoomSingleton.getRoom();
        room.removePlayer(split[1]);

        updateListView();
    }
}
