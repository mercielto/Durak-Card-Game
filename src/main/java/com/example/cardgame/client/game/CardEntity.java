package com.example.cardgame.client.game;

import com.example.cardgame.gameProperties.cards.Card;
import javafx.scene.image.ImageView;

public class CardEntity extends Card {
    private ImageView img;

    public CardEntity(ImageView imageView, Card card1) {
        super(card1.getValue(), card1.getSuit());
        img = imageView;
    }

    public ImageView getImg() {
        return img;
    }
}
