package com.example.cardgame.client.application;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.GameSingleton;
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
        GameSingleton.getGame();

        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Durak game");
        stage.setScene(scene);
        stage.show();
    }
}
