package com.example.cardgame.client.service;

import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.properties.FxmlObjectProperties;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class FxmlObjectsGetter {
    private static Stage stage = StageSingleton.getStage();

    public static void setStage(Stage stage1) {
        stage = stage1;
    }

    public static ListView<String> getListViewById(String id) {
        Scene scene = stage.getScene();
        return (ListView<String>) scene.lookup(id);
    }

    public static TextField getTextFieldById(String id) {
        Scene scene = stage.getScene();
        return (TextField) scene.lookup(id);
    }

    public static ChoiceBox<String> getChoiceBoxById(String id) {
        Scene scene = stage.getScene();
        return (ChoiceBox<String>) scene.lookup(id);
    }

    public static Button getButtonById(String id) {
        Scene scene = stage.getScene();
        return (Button) scene.lookup(id);
    }
}
