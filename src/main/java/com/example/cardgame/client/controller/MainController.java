package com.example.cardgame.client.controller;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.client.application.CreateRoomApplication;
import com.example.cardgame.client.application.JoinRoomApplication;
import com.example.cardgame.client.request.generator.ClientMenuRequestGenerator;
import com.example.cardgame.client.response.model.RoomResponse;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;


import com.example.cardgame.properties.SearchTypeProperty;

public class MainController {

    @FXML
    private ListView<String> listViewRooms;
    @FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> searchType;

    @FXML
    protected void handleSearch() {
        SearchTypeProperty property = SearchTypeProperty.define(String.valueOf(searchType.getValue()));
        String searchParameter = searchTextField.getText();

        Client client = ClientSingleton.getClient();
        client.write(ClientMenuRequestGenerator.getAvailableRooms(property, searchParameter));
    }

    @FXML
    public void handleMouseClickListView(MouseEvent mouseEvent) throws Exception {
        if (mouseEvent.getClickCount() == 2) {
            RoomResponse response = RoomResponse.parse(listViewRooms.getSelectionModel().getSelectedItem());
            (new JoinRoomApplication(response)).start(StageSingleton.getStage());
        }
    }
    @FXML
    public void createRoom() throws Exception {
        CreateRoomApplication app = new CreateRoomApplication();
        app.start(StageSingleton.getStage());
    }
}