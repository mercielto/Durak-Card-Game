package com.example.cardgame.client.controller;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientRoomSingleton;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.client.application.JoinRoomApplication;
import com.example.cardgame.client.application.MainApplication;
import com.example.cardgame.client.request.generator.ClientMenuRequestGenerator;
import com.example.cardgame.client.response.model.RoomResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;


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
    private Button readyBtn;
    @FXML
    private Button createBtn;


    @FXML
    private void backToSearch() throws IOException {
        Client client = ClientSingleton.getClient();
        if (createBtn.isDisable()) {
            client.write(ClientMenuRequestGenerator.leaveRoom());
        }
        ClientRoomSingleton.setRoom(null);
        (new MainApplication()).start(StageSingleton.getStage());
    }

    @FXML
    private void createRoom() {
        Client client = ClientSingleton.getClient();
        client.write(ClientMenuRequestGenerator
                .createRoom(
                        roomName.getText(),
                        Integer.valueOf(maxPlayersCount.getValue()))
        );
    }

    @FXML
    private void handleReadyButtonAction() {
        Client client = ClientSingleton.getClient();
        client.write(
                ClientMenuRequestGenerator.ready()
        );
    }
}
