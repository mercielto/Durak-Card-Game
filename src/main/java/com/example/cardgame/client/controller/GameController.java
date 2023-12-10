package com.example.cardgame.client.controller;

import com.example.cardgame.client.GameSingleton;
import com.example.cardgame.client.game.CardImageView;
import com.example.cardgame.client.game.ClientGame;
import com.example.cardgame.client.timerTask.AlertRemovingTask;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.server.service.ServerGameService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    @FXML
    private AnchorPane tableCardsPane;
    @FXML
    private AnchorPane handCardsPane;
    @FXML
    private Label alertLabel;
    @FXML
    private Label informationLabel;

    @FXML
    public void onTableClickHandler() {
        ClientGame game = GameSingleton.getGame();
        CardImageView selectedCard = game.getSelectedCard();
        ImageView imageView = selectedCard.getImg();

        if (game.getSelectedCard() == null) {
            alertLabel.setText("CARD NOT SELECTED");
            Timer timer = new Timer();
            timer.schedule(new AlertRemovingTask(alertLabel), 5000);
            return;
        }


        if (!game.getCanMove()) {
            alertLabel.setText("NOT YOUR MOVE YET");
            Timer timer = new Timer();
            timer.schedule(new AlertRemovingTask(alertLabel), 5000);
            return;
        }

        Card card = selectedCard.getCard();

        handCardsPane.getChildren().remove(imageView);
        tableCardsPane.getChildren().add(imageView);
        imageView.setY(0);

        game.addCardOnTable(selectedCard);
        ServerGameService.newCardOnTable(card);
    }

    private List<Card> getCardsOnTable() {
        ObservableList<Node> nodes = tableCardsPane.getChildren();
        List<Card> cards = new ArrayList<>();

        for (Node node : nodes) {
            cards.add(Card.getCard(node.getAccessibleText()));
        }

        return cards;
    }

    private ImageView getSelectedCardView() {
        ObservableList<Node> cards = handCardsPane.getChildren();
        for (Node node : cards) {
            ImageView view = (ImageView) node;
            if (node.getAccessibleHelp() != null) {
                if (node.getAccessibleHelp().equals("s")) {
                    return view;
                }
            }
        }
        return null;
    }
}
