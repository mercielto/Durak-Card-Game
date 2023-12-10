package com.example.cardgame.client.game;

import com.example.cardgame.client.exception.CardNotSelectedException;
import com.example.cardgame.gameProperties.cards.Card;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ClientGame {
    private List<CardImageView> cardsOnHands = new ArrayList<>();
    private List<CardImageView> cardsOnTable = new ArrayList<>();
    private CardImageView selectedCard;
    private CardImageView trumpCard;
    private boolean canMove = false;

    public ClientGame() {}

    public List<CardImageView> getCardsOnHands() {
        return cardsOnHands;
    }

    public List<CardImageView> getCardsOnTable() {
        return cardsOnTable;
    }

    public void addCardOnHand(Card card, ImageView imageView) {
        cardsOnHands.add(
                new CardImageView(imageView, card)
        );
    }

    public void addCardOnTable(Card card, ImageView imageView) {
        cardsOnTable.add(
                new CardImageView(imageView, card)
        );
    }

    public void addCardOnTable(CardImageView card) {
        cardsOnTable.add(card);
        cardsOnHands.remove(card);
    }

    public CardImageView getCardFromHands(ImageView imageView) {
        for (CardImageView cardImageView : cardsOnHands) {
            if (cardImageView.getImg().equals(imageView)) {
                return cardImageView;
            }
        }
        throw new RuntimeException("There is no such card as " + imageView);
    }

    public CardImageView getCardFromTable(ImageView imageView) {
        for (CardImageView cardImageView : cardsOnTable) {
            if (cardImageView.getImg().equals(imageView)) {
                return cardImageView;
            }
        }
        throw new RuntimeException("There is no such card as " + imageView);
    }

    public CardImageView getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(CardImageView selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setSelectedCard(ImageView img) {
        selectedCard = getCardFromHands(img);
    }

    public CardImageView getTrumpCard() {
        return trumpCard;
    }

    public void setTrumpCard(Card card, ImageView img) {
        trumpCard = new CardImageView(img, card);
    }

    public boolean getCanMove() {
        return canMove;
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }
}
