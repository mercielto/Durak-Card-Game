package com.example.cardgame.client.timerTask;

import javafx.application.Platform;
import javafx.scene.control.Label;

import java.util.TimerTask;

public class AlertRemovingTask extends TimerTask {
    private Label label;

    public AlertRemovingTask(Label label1) {
        label = label1;
    }

    @Override
    public void run() {
        Platform.runLater(() -> label.setText(null));

    }
}
