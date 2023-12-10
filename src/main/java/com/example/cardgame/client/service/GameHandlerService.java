package com.example.cardgame.client.service;

import com.example.cardgame.client.GameSingleton;
import com.example.cardgame.client.exception.CardNotSelectedException;
import com.example.cardgame.client.game.CardImageView;
import com.example.cardgame.client.game.ClientGame;
import com.example.cardgame.client.timerTask.AlertRemovingTask;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.properties.FxmlObjectProperties;
import com.example.cardgame.properties.ServerProperties;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Timer;

public class GameHandlerService {
    public static void setCardsOnGameStart(Card trumpCard, List<Card> cards) {
        ImageView trumpCardPlace = FxmlObjectsGetter.getImageViewById(FxmlObjectProperties.trumpCardId);
        try {
            trumpCardPlace.setImage(
                    new Image(
                            new FileInputStream(
                                    ServerProperties.getCardImagesStoragePath() + "\\"+ trumpCard.getImageName()
                            )
                    )
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ClientGame game = GameSingleton.getGame();
        game.setTrumpCard(trumpCard, trumpCardPlace);

        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.handCardsAnchorPaneId);
        double size = pane.getWidth();
        double spacing = size / cards.size();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);
            imageView.setX(spacing * i);

            imageView.setOnMouseClicked(mouseEvent -> {
                ImageView img = (ImageView) mouseEvent.getSource();
                img.setLayoutY(-30);

                CardImageView cardImageView = game.getSelectedCard();
                if (cardImageView != null) {
                    cardImageView.getImg().setLayoutY(0);
                }

                game.setSelectedCard(img);
            });

            game.addCardOnHand(card, imageView);

            pane.getChildren().add(imageView);
        }
    }

    public static void handleYourMove() {
        GameSingleton.getGame().setCanMove(true);
        Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.informationLabelId);
        label.setText("YOUR MOVE");
    }

//    public static void cardIsNotSelectedExceptionHandler() {
//        Label alertLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.alertLabelId);
//        alertLabel.setText("CARD NOT SELECTED");
//        Timer timer = new Timer();
//        timer.schedule(new AlertRemovingTask(alertLabel), 5000);
//    }

    public static void handleNotYourMove() {
    }

    public static void handleNewCardOnTable(String[] message) {
        Card card = Card.getCard(message[1]);
        ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);

        ClientGame game = GameSingleton.getGame();
        game.addCardOnTable(card, imageView);

        updateTableCards();
    }

    public static void updateTableCards() {
        ClientGame game = GameSingleton.getGame();
        AnchorPane table = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
        int count = 0;
        for (CardImageView cardImageView : game.getCardsOnTable()) {
            ImageView img = cardImageView.getImg();
            double pos = table.getWidth() / table.getChildren().size();
            img.setX(pos * count);
            count++;
            table.getChildren().add(img);
        }
    }
}
