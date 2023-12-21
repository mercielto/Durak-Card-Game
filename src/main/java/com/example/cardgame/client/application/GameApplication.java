package com.example.cardgame.client.application;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.ClientGameSingleton;
import com.example.cardgame.client.listener.ClientGameListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Client client = ClientSingleton.getClient();
        client.setInputListener(new ClientGameListener());
        ClientGameSingleton.getGame();

        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Durak game (%s)".formatted(ClientSingleton.getClient().getName()));
        stage.setScene(scene);
        stage.show();
    }
}
