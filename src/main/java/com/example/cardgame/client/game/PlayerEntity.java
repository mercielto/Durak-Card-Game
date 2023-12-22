package com.example.cardgame.client.game;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerEntity {
    private final String name;
    private Label cardsCountLabel;
    private int cardsCount;
    private String imageName;
    private AnchorPane pane;

    public PlayerEntity(String name, Label cardsCountLabel) {
        this.name = name;
        this.cardsCountLabel = cardsCountLabel;
    }

    public PlayerEntity(String name) {
        this.name = name;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }

    public void addBorder() {
        if (pane != null) {
            System.out.println("BORDER IS ADDED!!!!!1");
            pane.setBorder(new Border(new BorderStroke(Paint.valueOf("#ff0000"),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    public void removeBorder() {
        if (pane != null) {
            pane.setBorder(Border.EMPTY);
        }
    }

    public void reduceCardsCount() {
        cardsCount--;
        cardsCountLabel.setText(String.valueOf(cardsCount));
    }

    public void addCardsCount(int count) {
        cardsCount += count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlayerEntity player) {
            return player.getName().equals(name);
        }
        return false;
    }

    public void addCards(int i) {
        cardsCount += i;
        cardsCountLabel.setText(String.valueOf(cardsCount));
    }
}
