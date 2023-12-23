package com.example.cardgame.client.controller;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.request.generator.ClientMenuRequestGenerator;
import com.example.cardgame.client.timerTask.AlertRemovingTask;
import com.example.cardgame.properties.ServerProperties;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Timer;

import static com.example.cardgame.client.service.MenuHandlerService.setAlert;

public class SetNameApplicationController {
    @FXML
    private TextField nameInputAreaId;
    @FXML
    private Label alertLabelId;

    @FXML
    private void checkName() {
        String name = nameInputAreaId.getText();

        if (name.length() < 3) {
            setAlert(alertLabelId, "Too short");
            return;
        }

        if (name.contains(ServerProperties.getMainDelimiter()) || name.contains(ServerProperties.getSideDelimiter())) {
            setAlert(alertLabelId, "You can not use %s and %s symbols".formatted(ServerProperties.getMainDelimiter(),
                    ServerProperties.getSideDelimiter()));
            return;
        }

        Client client = ClientSingleton.getClient();
        client.write(
                ClientMenuRequestGenerator.setName(name)
        );
    }


}
