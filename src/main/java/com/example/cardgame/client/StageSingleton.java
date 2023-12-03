package com.example.cardgame.client;

import javafx.stage.Stage;

public class StageSingleton {
    private static Stage stage;

    public static Stage getStage() {
        if (stage == null) {
            stage = new Stage();
        }

        return stage;
    }

    public static void setStage(Stage newStage) {
        stage = newStage;
    }
}
