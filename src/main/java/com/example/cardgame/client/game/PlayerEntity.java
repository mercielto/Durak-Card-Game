package com.example.cardgame.client.game;

import javafx.scene.control.Label;

public class PlayerEntity {
    private final String name;
    private Label cardsCountLabel;
    private int cardsCount;
    private String imageName;

    public PlayerEntity(String name, Label cardsCountLabel) {
        this.name = name;
        this.cardsCountLabel = cardsCountLabel;
    }

    public PlayerEntity(String name) {
        this.name = name;
    }

    public void reduceCardsCount() {
        cardsCount--;
        cardsCountLabel.setText(String.valueOf(cardsCount));
    }

    public Label getCardsCountLabel() {
        return cardsCountLabel;
    }

    public void setCardsCountLabel(Label cardsCountLabel) {
        this.cardsCountLabel = cardsCountLabel;
    }

    public String getName() {
        return name;
    }

    public int getCardsCount() {
        return cardsCount;
    }

    public void setCardsCount(int cardsCount) {
        this.cardsCount = cardsCount;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlayerEntity player) {
            return player.getName().equals(name);
        }
        return false;
    }
}
