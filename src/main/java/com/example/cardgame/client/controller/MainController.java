package com.example.cardgame.client.controller;

import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.client.application.CreateRoomApplication;
import com.example.cardgame.client.application.JoinRoomApplication;
import com.example.cardgame.client.application.MainApplication;
import com.example.cardgame.client.response.model.RoomResponse;
import com.example.cardgame.client.service.MenuHandlerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.List;

import com.example.cardgame.properties.SearchTypeProperty;

public class MainController {

    @FXML
    private ListView<String> listView;
    @FXML
    private TextField searchTextField;
    @FXML
    private ChoiceBox<String> searchType;

    @FXML
    protected void handleSearch() {
        SearchTypeProperty property = SearchTypeProperty.define(String.valueOf(searchType.getValue()));
        String searchParameter = searchTextField.getText();

        List<String> rooms = MenuHandlerService.getAvailableRooms(property, searchParameter);
        ObservableList<String> roomsObservableList = FXCollections.observableArrayList(rooms);
        listView.setItems(roomsObservableList);
    }

    @FXML
    public void handleMouseClickListView(MouseEvent mouseEvent) throws Exception {
        if (mouseEvent.getClickCount() == 2) {
            RoomResponse response = RoomResponse.parse(listView.getSelectionModel().getSelectedItem());
            MenuHandlerService.joinRoom(response);
            (new JoinRoomApplication(response)).start(StageSingleton.getStage());
        }
    }
    @FXML
    public void createRoom() throws Exception {
        CreateRoomApplication app = new CreateRoomApplication();
        app.start(StageSingleton.getStage());
    }
}