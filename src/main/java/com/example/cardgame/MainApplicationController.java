package com.example.cardgame;

import com.example.cardgame.client.service.MenuHandlerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.List;

import com.example.cardgame.properties.SearchTypeProperty;

public class MainApplicationController {

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
    public void handleMouseClickListView(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            System.out.println("clicked on " + listView.getSelectionModel().getSelectedItem());
        }
    }
}