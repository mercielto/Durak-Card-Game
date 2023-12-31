package com.example.cardgame.client.service;

import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.client.game.PlayerEntity;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.client.FxmlObjectProperties;
import com.example.cardgame.properties.ServerProperties;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

    public static ImageView getImageViewById(String id) {
        Scene scene = stage.getScene();
        return (ImageView) scene.lookup(id);
    }

    public static Label getLabelById(String id) {
        Scene scene = stage.getScene();
        return (Label) scene.lookup(id);
    }

    public static AnchorPane getAnchorPaneById(String id) {
        Scene scene = stage.getScene();
        return (AnchorPane) scene.lookup(id);
    }

    public static ImageView createImageViewForCard(Card card) {
        ImageView img = new ImageView();
        try {
            img.setImage(
                    new Image(
                            new FileInputStream(
                                    ServerProperties.getCardImagesStoragePath() + "\\"+ card.getImageName()
                            )
                    )
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        img.setFitHeight(FxmlObjectProperties.CARD_HEIGHT);
        img.setFitWidth(FxmlObjectProperties.CARD_WIDTH);

        img.setX(0);
        img.setY(0);
        return img;
    }

    public static AnchorPane createAnchorPaneForUserAvatar() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPrefSize(
                FxmlObjectProperties.ANCHOR_PANE_FOR_USER_WIDTH,
                FxmlObjectProperties.ANCHOR_PANE_FOR_USER_HEIGHT
        );
        return anchorPane;
    }

    public static ImageView createImageViewForUserAvatar(String imageName) {
        ImageView img = new ImageView();
        try {
            img.setImage(
                    new Image(
                            new FileInputStream(
                                    ServerProperties.getAvatarImagesStoragePath() + imageName
                            )
                    )
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        img.setFitHeight(FxmlObjectProperties.USER_IMAGE_IN_GAME_HEIGHT);
        img.setFitWidth(FxmlObjectProperties.USER_IMAGE_IN_GAME_WIDTH);
        return img;
    }

    public static AnchorPane createAnchorPaneForUser(PlayerEntity player) {
        AnchorPane pane = createAnchorPaneForUserAvatar();
//        pane.setBorder(Border.stroke(Paint.valueOf("#000000")));

        Label nameLabel = new Label(player.getName());

        nameLabel.setTextFill(Paint.valueOf("#ffffff"));
        nameLabel.setLayoutY(48);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setPrefWidth(FxmlObjectProperties.USER_NAME_LABEL_WIDTH);
        nameLabel.setPrefHeight(FxmlObjectProperties.USER_NAME_LABEL_HEIGHT);
//        nameLabel.setBorder(Border.stroke(Paint.valueOf("#000000")));

        Label cardsCountLabel = new Label(String.valueOf(player.getCardsCount()));

        cardsCountLabel.setPrefWidth(FxmlObjectProperties.USER_NAME_LABEL_WIDTH);
        cardsCountLabel.setPrefHeight(FxmlObjectProperties.USER_NAME_LABEL_HEIGHT);
        cardsCountLabel.setLayoutY(76);
        cardsCountLabel.setAlignment(Pos.CENTER);
        cardsCountLabel.setTextFill(Paint.valueOf("#ffffff"));

        player.setCardsCountLabel(cardsCountLabel);

        ImageView imageView = createImageViewForUserAvatar(player.getImageName());

        imageView.setLayoutY(0);
        imageView.setLayoutX(9);

        pane.getChildren().addAll(nameLabel, imageView, cardsCountLabel);
        return pane;
    }
}
