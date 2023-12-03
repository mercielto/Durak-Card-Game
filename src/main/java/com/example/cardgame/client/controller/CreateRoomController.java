package com.example.cardgame.client.controller;

import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.client.service.MenuHandlerService;
import com.example.cardgame.properties.commands.MenuCommands;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class CreateRoomController {
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField roomId;
    @FXML
    private TextField roomName;
    @FXML
    private ChoiceBox<String> maxPlayersCount;
    @FXML
    private Button createBtn;

    @FXML
    private void backToSearch() {

    }

    @FXML
    private void createRoom() {
        String id = MenuHandlerService.createRoom(roomName.getText(), Integer.valueOf(maxPlayersCount.getValue()));

        if (id.equals(MenuCommands.ERROR.getValue())) {
            return;
        }

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

    @FXML
    private void startGame() {

    }
}
