package com.example.cardgame.client.controller;

import com.example.cardgame.client.*;
import com.example.cardgame.client.application.JoinRoomApplication;
import com.example.cardgame.client.game.CardEntity;
import com.example.cardgame.client.game.ClientGame;
import com.example.cardgame.client.listener.ClientMenuListener;
import com.example.cardgame.client.request.generator.ClientGameRequestGenerator;
import com.example.cardgame.client.service.GameHandlerService;
import com.example.cardgame.gameProperties.cards.Card;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

import static com.example.cardgame.client.service.GameHandlerService.*;

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
        if (!canAddCardOnTable(alertLabel)) {
            return;
        }

        ClientGame game = ClientGameSingleton.getGame();
        CardEntity selectedCard = game.getSelectedCard();

        if (!game.canBeAddedToTable(selectedCard)) {
            setAlert("YOU CAN NOT MOVE THIS CARD");
            return;
        }


        selectedCard.getImg().setLayoutY(0);

        GameHandlerService.addNewCardOnTable(selectedCard.getImg(), tableCardsPane);
        handCardsPane.getChildren().remove(selectedCard.getImg());
        selectedCard.getImg().setOnMouseClicked(null);
        game.addCardOnTable(selectedCard);
        game.removeSelectedCard();
        GameHandlerService.newCardOnTable(selectedCard);
        GameHandlerService.removeButtonsFromPositionPane();
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

    @FXML
    public void backImageActionHandler() throws Exception {
        Client client = ClientSingleton.getClient();
        client.write(
                ClientGameRequestGenerator.quitGame()
        );

        ClientGameSingleton.clear();

        client.setInputListener(new ClientMenuListener());

        (new JoinRoomApplication(ClientRoomSingleton.getRoom())).start(StageSingleton.getStage());
    }
}
