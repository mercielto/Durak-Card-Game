package com.example.cardgame.client.game;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.gameProperties.cards.CardPair;
import com.example.cardgame.server.exception.PlayerNotFoundException;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ClientGame {
    private List<CardEntity> cardsOnHands = new ArrayList<>();
    private List<CardPair> cardsOnTable = new ArrayList<>();
    private CardEntity selectedCard;
    private CardEntity trumpCard;
    private List<PlayerEntity> players = new ArrayList<>();
    private int currentPlayer = 0;
    private boolean canMove = false;

    public ClientGame() {}

    public void setPlayers(List<PlayerEntity> players1) {
        players = players1;
    }

    public void setCurrentPlayer(int n) {
        currentPlayer = n;
    }

    public List<CardEntity> getCardsOnHands() {
        return cardsOnHands;
    }

    public List<CardPair> getCardsOnTable() {
        return cardsOnTable;
    }

    public void addCardOnHand(Card card, ImageView imageView) {
        cardsOnHands.add(
                new CardEntity(imageView, card)
        );
    }

    public void addCardOnTable(Card card, ImageView imageView) {
        cardsOnTable.add(
                new CardPair(new CardEntity(imageView, card), trumpCard.getSuit())
        );
    }

    public void addCardOnTable(CardEntity card) {
        cardsOnTable.add(new CardPair(card, trumpCard.getSuit()));
        removeCardFromHands(card);
    }

    public boolean canBeAddedToTable(Card card) {
        if (cardsOnTable.size() == 0) {
            return true;
        }

        for (CardPair cardPair : cardsOnTable) {
            Card firstCard = cardPair.getFirst();
            if (firstCard.getValue().equals(card.getValue())) {
                return true;
            }

            Card secondCard = cardPair.getSecond();
            if (secondCard != null) {
                if (secondCard.getValue().equals(card.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public CardEntity getCardFromHands(ImageView imageView) {
        for (CardEntity cardEntity : cardsOnHands) {
            if (cardEntity.getImg().equals(imageView)) {
                return cardEntity;
            }
        }
        throw new RuntimeException("There is no such card as " + imageView);
    }

    public boolean beatCardOnTable(CardEntity cardToBeat, CardEntity card) {
        for (CardPair cardPair : cardsOnTable) {
            if (cardPair.getFirst().equals(cardToBeat)) {
                if (cardPair.beats(card)) {
                    selectedCard = null;
                    return true;
                }
            }
        }
        return false;
    }

    public CardEntity getCardFromTable(ImageView imageView) {
        for (CardPair cardPair : cardsOnTable) {
            CardEntity cardEntity = (CardEntity) cardPair.getFirst();
            if (cardEntity.getImg().equals(imageView)) {
                return cardEntity;
            }
        }
        throw new RuntimeException("There is no such card as " + imageView);
    }

    public CardEntity getCardFromTable(Card card) {
        for (CardPair cardPair : cardsOnTable) {
            CardEntity cardEntity = (CardEntity) cardPair.getFirst();
            if (cardEntity.equals(card)) {
                return cardEntity;
            }
        }
        throw new RuntimeException("There is no such card as " + card.toString());
    }

    public CardEntity getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(CardEntity selectedCard) {
        this.selectedCard = selectedCard;
    }

    public void setSelectedCard(ImageView img) {
        selectedCard = getCardFromHands(img);
    }

    public void removeSelectedCard() {
        selectedCard = null;
    }

    public CardEntity getTrumpCard() {
        return trumpCard;
    }

    public void setTrumpCard(Card card, ImageView img) {
        trumpCard = new CardEntity(img, card);
    }

    public boolean canAddCardOnTable() {
        Client client = ClientSingleton.getClient();
        return players.get(currentPlayer).equals(client.getName()) || canMove;
    }

    public boolean canBeatCard() {
        Client client = ClientSingleton.getClient();
        PlayerEntity defender = getDefender();
        return defender.getName().equals(client.getName());
    }

    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }

    public void next() {
        currentPlayer = (currentPlayer + 1) % players.size();
        PlayerEntity curPlayer = players.get(currentPlayer);

        removePlayerBorders();
        PlayerEntity defender = getDefender();
        defender.addBorder();

        System.out.println("Current player: " + curPlayer.getName());
    }

    public void removeCardFromHands(CardEntity card) {
        for (CardEntity cardOnHand : cardsOnHands) {
            if (cardOnHand.equals(card)) {
                cardsOnHands.remove(cardOnHand);
                return;
            }
        }
    }

    public PlayerEntity getPlayerByName(String name) throws PlayerNotFoundException {
        for (PlayerEntity player : players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        throw new PlayerNotFoundException(name);
    }

    public boolean isThereCardsToBeat() {
        for (CardPair cardPair : cardsOnTable) {
            if (cardPair.isFull()) {
                return true;
            }
        }
        return false;
    }

    public void takeTableCardsOnHands() {
        for (CardPair cardPair : cardsOnTable) {
            List<Card> cards = cardPair.getAll();

            for (Card card : cards) {
                cardsOnHands.add((CardEntity) card);
            }
        }
        cardsOnTable.clear();
    }

    public PlayerEntity getDefender() {
        int next = (currentPlayer + 1) % players.size();
        return players.get(next);
    }

    public int getCardsOnTableCount() {
        int count = 0;
        for (CardPair cardPair : cardsOnTable) {
            count += cardPair.getAll().size();
        }
        return count;
    }

    public void clearCardsOnTable() {
        cardsOnTable.clear();
    }

    public void removePlayer(PlayerEntity player) {
        int index = players.indexOf(player);
        players.remove(player);
        if (currentPlayer >= index) {
            currentPlayer--;
            if (currentPlayer < 0) {
                currentPlayer += players.size();
            }
        }
    }

    public void removePlayerBorders() {
        for (PlayerEntity player : players) {
            player.removeBorder();
        }
    }

    public PlayerEntity getCurrentPlayer() {
        return players.get(currentPlayer);
    }
}
