package com.example.cardgame.client.timerTask;

import com.example.cardgame.client.ClientRoomSingleton;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.client.application.JoinRoomApplication;
import com.example.cardgame.client.listener.ClientMenuListener;
import javafx.application.Platform;
import lombok.SneakyThrows;

import java.util.TimerTask;

public class EndOfMoveTask extends TimerTask {
    @SneakyThrows
    @Override
    public void run() {

        // Think should be removed, has unchecked transfers
        Platform.runLater(() ->
                {
                    try {
                        ClientSingleton.getClient().setInputListener(new ClientMenuListener());
                        (new JoinRoomApplication(ClientRoomSingleton.getRoom())).start(StageSingleton.getStage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }
}
