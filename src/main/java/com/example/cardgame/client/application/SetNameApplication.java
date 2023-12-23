package com.example.cardgame.client.application;

import com.example.cardgame.client.StageSingleton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SetNameApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        StageSingleton.setStage(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(SetNameApplication.class.getResource("setName.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();
    }
}
