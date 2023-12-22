package com.example.cardgame.client.application;

import com.example.cardgame.client.response.model.RoomResponse;
import com.example.cardgame.client.service.MenuHandlerService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class JoinRoomApplication extends Application {
    private RoomResponse room;
    
    public JoinRoomApplication(RoomResponse response) {
        room = response;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(JoinRoomApplication.class.getResource("createRoom.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MenuHandlerService.joinRoom(room);
        MenuHandlerService.sendRequestForListView(room.getUuid());

        ChoiceBox<String> checkBox = (ChoiceBox<String>) scene.lookup("#maxPlayersCount");
        checkBox.setDisable(true);
        checkBox.setValue(String.valueOf(room.getMaxPlayersCount()));
        
        Button btn = (Button) scene.lookup("#createBtn");
        btn.setDisable(true);
        btn.opacityProperty().setValue(0);
        
        TextField nameField = (TextField) scene.lookup("#roomName");
        nameField.setEditable(false);
        nameField.setText(room.getName());
        
        TextField idField = (TextField) scene.lookup("#roomId");
        idField.setText(room.getUuid().toString());

        stage.setTitle("Join room");
        stage.setScene(scene);

        if (room.getPlayers().size() == room.getMaxPlayersCount()) {
            MenuHandlerService.waitingForConfirmation();
        }
        stage.show();
    }
}
