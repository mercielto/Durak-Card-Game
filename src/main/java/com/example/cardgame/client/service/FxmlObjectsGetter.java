package com.example.cardgame.client.service;

import com.example.cardgame.client.StageSingleton;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.properties.FxmlObjectProperties;
import com.example.cardgame.properties.ServerProperties;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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
        img.setAccessibleText(card.toString());
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

        img.setFitHeight(FxmlObjectProperties.cardHeight);
        img.setFitWidth(FxmlObjectProperties.cardWidth);
        return img;
    }
}
