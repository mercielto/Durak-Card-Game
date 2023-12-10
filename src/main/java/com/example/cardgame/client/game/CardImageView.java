package com.example.cardgame.client.game;

import com.example.cardgame.gameProperties.cards.Card;
import javafx.scene.image.ImageView;

public class CardImageView {
    private ImageView img;
    private Card card;

    public CardImageView(ImageView imageView, Card card1) {
        img = imageView;
        card = card1;
    }

    public ImageView getImg() {
        return img;
    }

    public Card getCard() {
        return card;
    }
}
