package com.example.cardgame.client.service;

import com.example.cardgame.client.*;
import com.example.cardgame.client.game.CardEntity;
import com.example.cardgame.client.game.ClientGame;
import com.example.cardgame.client.game.PlayerEntity;
import com.example.cardgame.client.request.generator.ClientGameRequestGenerator;
import com.example.cardgame.client.timerTask.AlertRemovingTask;
import com.example.cardgame.client.timerTask.EndOfMoveTask;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.gameProperties.cards.CardPair;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.server.exception.PlayerNotFoundException;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class GameHandlerService {
    public static void newCardOnTable(Card card) {
        Client client = ClientSingleton.getClient();
        client.write(
                ClientGameRequestGenerator.newCardOnTable(card.toString())
        );
    }

    public static void setCardsOnGameStart(Card trumpCard, List<Card> cards) {
        ImageView trumpCardPlace = FxmlObjectsGetter.getImageViewById(FxmlObjectProperties.TRUMP_CARD);
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
        ClientGame game = ClientGameSingleton.getGame();
        game.setTrumpCard(trumpCard, trumpCardPlace);

        resetCardsOnHands(cards);
    }

    public static void updateCardsOnHands() {
        ClientGame game = ClientGameSingleton.getGame();
        resetCardsOnHands(new ArrayList<>(game.getCardsOnHands()));
    }

    public static void resetCardsOnHands(List<Card> cards) {
        ClientGame game = ClientGameSingleton.getGame();
        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.HAND_CARDS_PANE);
        pane.getChildren().clear();
        game.getCardsOnHands().clear();
        double size = pane.getWidth();
        double spacing = size / cards.size();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);
            imageView.setX(spacing * i);
            imageView.setY(0);

            imageView.setOnMouseClicked(getOnCardOnHandsMouseClickedEventHandler(game));

            game.addCardOnHand(card, imageView);

            pane.getChildren().add(imageView);
        }
    }

    public static void handleYourMove() {
        ClientGame game = ClientGameSingleton.getGame();
        game.setCanMove(true);

        Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.INFORMATION_LABEL);
        label.setText("YOUR MOVE");

        if (game.getCardsOnTable().size() != 0) {
            addBeatButton();
        }

    }

    public static void handleNotYourMove() {
    }

    public static void setLabel(Label alertLabel, TimerTask task, String text) {
        alertLabel.setText(text);
        Timer timer = new Timer();
        timer.schedule(task, 5000);
    }

    public static void setAlert(String text) {
        Label alertLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.ALERT_LABEL);
        setLabel(alertLabel, new AlertRemovingTask(alertLabel), text);
    }

    public static boolean canAddCardOnTable(Label alertLabel) {
        ClientGame game = ClientGameSingleton.getGame();
        if (game.getSelectedCard() == null) {
            setAlert("CARD NOT SELECTED");
            return false;
        }


        if (!game.canAddCardOnTable()) {
            setAlert("NOT YOUR MOVE YET");
            return false;
        }

        return true;
    }

    public static void handleNewCardOnTable(String[] message) {
        ClientGame game = ClientGameSingleton.getGame();

        Card card = Card.getCard(message[1]);
        PlayerEntity player;
        try {
            player = game.getPlayerByName(message[2]);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (game.canBeatCard()) {
            addTakeCardsButton();
        }

        player.reduceCardsCount();

        ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);
        AnchorPane table = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.TABLE_CARDS_PANE);

        imageView.setOnMouseClicked(mouseEvent -> {
            AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.TABLE_CARDS_PANE);
            
            Client client = ClientSingleton.getClient();

            if (game.canBeatCard()) {
                CardEntity selectedCard = game.getSelectedCard();
                CardEntity cardToBeat = game.getCardFromTable((ImageView) mouseEvent.getSource());

                if (game.beatCardOnTable(cardToBeat, selectedCard)) {
                    game.removeCardFromHands(selectedCard);

                    ImageView img = selectedCard.getImg();
                    setBeatCardPosition(img, pane, cardToBeat);

                    client.write(
                            ClientGameRequestGenerator.beatCard(cardToBeat, selectedCard)
                    );

                    if (game.isThereCardsToBeat()) {
                        addTakeCardsButton();
                    } else {
                        removeButtonsFromPositionPane();
                    }

                    if (game.getCardsOnHands().size() == 0) {
                        client.write(
                                ClientGameRequestGenerator.forcedEndOfMove()
                        );
                    }
                } else {
                    System.out.println("Alert: \"THIS CARD CANNOT BEAT THE SELECTED CARD\"");
                    setAlert("THIS CARD CANNOT BEAT THE SELECTED CARD");
                }
            }
        });

        CardEntity newCard = new CardEntity(imageView, card);
        addNewCardFromPlayerOnTable(imageView, table, player);
        ClientGameSingleton.getGame().addCardOnTable(newCard);
    }

    public static void removeButtonsFromPositionPane() {
        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.BUTTON_POSITION);
        pane.getChildren().clear();
    }

    public static void setTranslationOfCard(Node node, double fromX, double fromY, double toX, double toY) {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(250));
        translateTransition.setNode(node);

        translateTransition.setFromX(fromX);
        translateTransition.setFromY(fromY);

        translateTransition.setToX(toX);
        translateTransition.setToY(toY);

        translateTransition.setAutoReverse(false);
        translateTransition.play();
    }

    public static void addTakeCardsButton() {
        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.BUTTON_POSITION);
        ObservableList<Node> children = pane.getChildren();

        Button button = new Button("Take cards");
        button.setOnAction(actionEvent -> {
            ClientGame game = ClientGameSingleton.getGame();
            List<CardPair> tableCardsPairs = new ArrayList<>(game.getCardsOnTable());
            List<CardEntity> cards = new ArrayList<>(game.getCardsOnHands());

            game.next();
            game.next();
            children.clear();
            ClientSingleton.getClient().write(
                    ClientGameRequestGenerator.takeCards()
            );

            AnchorPane handsCardsPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.HAND_CARDS_PANE);
            AnchorPane tableCardsPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.TABLE_CARDS_PANE);

            List<Card> tableCards = new ArrayList<>();
            for (CardPair pair : tableCardsPairs) {
                tableCards.addAll(pair.getAll());
            }

            double size = handsCardsPane.getWidth();
            double spacing = size / (cards.size() + tableCards.size());

            setTranslationToCards(cards, spacing);

            for (int i = 0; i < tableCards.size(); i++) {
                CardEntity cardEntity = (CardEntity) tableCards.get(i);
                ImageView cardImage = cardEntity.getImg();

                double fromX = cardImage.getX();
                double fromY = tableCardsPane.getLayoutY() - handsCardsPane.getLayoutY();

                double toX = spacing * (i + cards.size());
                double toY = cardImage.getY();

                setTranslationOfCard(cardImage, fromX, fromY, toX, toY);

                handsCardsPane.getChildren().add(cardImage);
                cardImage.setOnMouseClicked(getOnCardOnHandsMouseClickedEventHandler(game));

                cardImage.setX(0);
                cardImage.setY(0);
                cardImage.setLayoutY(0);
            }

            game.takeTableCardsOnHands();
        });

        children.clear();
        children.add(button);
    }

    public static void setTranslationToCards(List<CardEntity> cards, double spacing) {
        AnchorPane handsPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.HAND_CARDS_PANE);
        double previousSpacing = handsPane.getWidth() / cards.size();
        for (int i = 0; i < cards.size(); i++) {
            CardEntity card = cards.get(i);
            ImageView cardImage = card.getImg();

            double fromX = previousSpacing * i;
            double fromY = 0;

            double toX = spacing * i;
            double toY = cardImage.getY();

            setTranslationOfCard(cardImage, fromX, fromY, toX, toY);

            cardImage.setX(0);
            cardImage.setY(0);
        }
    }

    private static EventHandler<? super MouseEvent> getOnCardOnHandsMouseClickedEventHandler(ClientGame game) {
        return mouseEvent ->  {
            ImageView img = (ImageView) mouseEvent.getSource();

            CardEntity cd = game.getSelectedCard();
            if (cd != null) {
                cd.getImg().setLayoutY(0);
            }

            img.setLayoutY(-30);

            game.setSelectedCard(img);
        };
    }

    public static void setBeatCardPosition(ImageView img, AnchorPane pane, CardEntity cardToBeat) {
        AnchorPane handsPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.HAND_CARDS_PANE);

        double fromX = img.getX() - img.getLayoutX();
        double fromY = handsPane.getLayoutY() - pane.getLayoutY() + img.getLayoutY();

        double toX = cardToBeat.getImg().getX() * 2;
        double toY = cardToBeat.getImg().getY();

        setTranslationOfCard(img, fromX, fromY, toX, toY);

        img.setX(0);
        img.setY(0);
        img.setLayoutY(10);
        img.setLayoutX(10);
        img.setOnMouseClicked(null);

        pane.getChildren().add(img);
        cardToBeat.getImg().setOnMouseClicked(null);
    }

    public static void addNewCardFromPlayerOnTable(ImageView imageView, AnchorPane table, PlayerEntity player) {
        ClientGame game = ClientGameSingleton.getGame();

        double pos = table.getWidth() / 12;

        AnchorPane playerPane = player.getPane();
        table.getChildren().add(imageView);

        double paneX = playerPane.getParent().getLayoutX() + playerPane.getLayoutX();
        double paneY = playerPane.getParent().getLayoutY() + playerPane.getLayoutY();

        double toX = pos * game.getCardsOnTable().size();
        double toY = 0;

        double fromX = paneX - table.getLayoutX() - toX;
        double fromY = paneY - table.getLayoutY();

        imageView.setX(toX);
        imageView.setY(toY);
        imageView.setLayoutY(0);

        setTranslationOfCard(imageView, fromX, fromY, toX, toY);
    }

    public static void addNewCardOnTable(ImageView imageView, AnchorPane table) {
        ClientGame game = ClientGameSingleton.getGame();

        double pos = table.getWidth() / 6;

        AnchorPane handsPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.HAND_CARDS_PANE);

        double fromX = imageView.getX();
        double fromY = handsPane.getLayoutY() - table.getLayoutY() - imageView.getLayoutY();

        double toX = pos * game.getCardsOnTable().size();
        double toY = 0;

        table.getChildren().add(imageView);

        setTranslationOfCard(imageView, fromX, fromY, toX, toY);

        imageView.setX(0);
        imageView.setY(0);
    }

    public static void setPlayersOrder(String players) {
        String[] split = players.split(ServerProperties.getSideDelimiter());
        ClientGame game = ClientGameSingleton.getGame();

        List<PlayerEntity> playerEntities = new ArrayList<>();

        List<String> playersList = new ArrayList<>(List.of(split));

        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.PLAYERS_LIST_PANE);

        double width = pane.getWidth();
        double personalWidth = width / (playersList.size());
        int cnt = 1;

        int index = playersList.indexOf(ClientSingleton.getClient().getName());
        for (int i = index; i < index + playersList.size(); i++) {
            int n = i % playersList.size();
            String name = playersList.get(n);
            PlayerEntity player = new PlayerEntity(name);
            player.setCardsCount(6);
            player.setImageName("avatar_01.png");

            if (!name.equals(ClientSingleton.getClient().getName())) {
                AnchorPane personalPane = FxmlObjectsGetter.createAnchorPaneForUser(player);
                personalPane.setLayoutY(6);
                personalPane.setLayoutX(cnt * personalWidth - FxmlObjectProperties.ANCHOR_PANE_FOR_USER_WIDTH / 2);
                pane.getChildren().add(personalPane);
                player.setPane(personalPane);
                cnt++;
            }
            playerEntities.add(player);

            if (name.equals(split[0])) {
                game.setCurrentPlayer(playerEntities.indexOf(player));
            }
        }
        game.setPlayers(playerEntities);

        PlayerEntity defender = game.getDefender();
        defender.addBorder();

        StringJoiner joiner = new StringJoiner(", ");

        for (PlayerEntity playerEntity : playerEntities) {
            joiner.add(playerEntity.getName());
        }
        System.out.println("Players order set: " + joiner);
        System.out.println("Current player: " + split[0]);
    }

    public static void handleBeatCard(String[] split) {
        ClientGame game = ClientGameSingleton.getGame();

        PlayerEntity player;
        try {
            player = game.getPlayerByName(split[1]);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        player.reduceCardsCount();
        Card cardToBeat = Card.getCard(split[2]);
        Card card = Card.getCard(split[3]);

        ImageView cardImage = FxmlObjectsGetter.createImageViewForCard(card);

        CardEntity cardToBeatEntity = ClientGameSingleton.getGame().getCardFromTable(cardToBeat);
        CardEntity cardEntity = new CardEntity(cardImage, card);

        game.beatCardOnTable(cardToBeatEntity, cardEntity);

        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.TABLE_CARDS_PANE);
        setBeatCardFromPlayerPosition(cardImage, pane, cardToBeatEntity, player);

        if (game.canAddCardOnTable()) {
            addBeatButton();
        }
    }

    private static void setBeatCardFromPlayerPosition(ImageView img, AnchorPane pane,
                                                      CardEntity cardToBeat, PlayerEntity player) {
        AnchorPane playerPane = player.getPane();

        double paneX = playerPane.getParent().getLayoutX() + playerPane.getLayoutX();
        double paneY = playerPane.getParent().getLayoutY() + playerPane.getLayoutY();

        double fromX = paneX - pane.getLayoutX();
        double fromY = paneY - pane.getLayoutY();

        double toX = cardToBeat.getImg().getX();
        double toY = cardToBeat.getImg().getY();

        setTranslationOfCard(img, fromX, fromY, toX, toY);

        img.setX(cardToBeat.getImg().getX());
        img.setY(cardToBeat.getImg().getY());
        img.setLayoutY(10);
        img.setLayoutX(10);
        img.setOnMouseClicked(null);

        pane.getChildren().add(img);
        cardToBeat.getImg().setOnMouseClicked(null);
    }

    private static void addBeatButton() {
        AnchorPane buttonPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.BUTTON_POSITION);
        buttonPane.getChildren().clear();
        Button button = new Button("BEAT");
        button.setOnAction(actionEvent -> {
            Button btn = (Button) actionEvent.getSource();
            AnchorPane parent = (AnchorPane) btn.getParent();
            parent.getChildren().clear();

            Client client = ClientSingleton.getClient();
            client.write(
                    ClientGameRequestGenerator.endMove()
            );
        });
        buttonPane.getChildren().add(button);
    }

    public static void handleAddNewCardsOnHands(String[] split) {
        ClientGame game = ClientGameSingleton.getGame();

        AnchorPane handsCardsPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.HAND_CARDS_PANE);

        double size = handsCardsPane.getWidth();
        double spacing = size / (split.length + game.getCardsOnHands().size());

        setTranslationToCards(game.getCardsOnHands(), spacing);
        for (int i = 1; i < split.length; i++) {
            Card card = Card.getCard(split[i]);
            ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);

            CardEntity cardEntity = new CardEntity(imageView, card);
            ImageView cardImage = cardEntity.getImg();

            double fromX = FxmlObjectProperties.DECK_X - handsCardsPane.getLayoutX();
            double fromY = FxmlObjectProperties.DECK_Y - handsCardsPane.getLayoutY();

            double toX = spacing * (i + game.getCardsOnHands().size() - 1);
            double toY = 0;

            setTranslationOfCard(cardImage, fromX, fromY, toX, toY);

            handsCardsPane.getChildren().add(cardImage);
            cardImage.setOnMouseClicked(getOnCardOnHandsMouseClickedEventHandler(game));

            cardImage.setX(0);
            cardImage.setY(0);
            cardImage.setLayoutY(0);

            game.addCardOnHand(card, imageView);
        }

        if (game.getCardsOnHands().size() == 0) {
            ClientSingleton.getClient().write(
                    ClientGameRequestGenerator.noMoreCardsOnHands()
            );
        }
    }

    public static void handleEndMove() {
        AnchorPane table = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.TABLE_CARDS_PANE);
        table.getChildren().clear();
        ClientGame game = ClientGameSingleton.getGame();
        game.getCardsOnTable().clear();
        game.setCanMove(false);
        game.removePlayerBorders();
        game.next();

        Label informationLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.INFORMATION_LABEL);
        informationLabel.setText(null);

        removeButtonsFromPositionPane();
        updateCardsOnHands();
    }

    public static void handleNewCardToUser(String[] split) {
        ClientGame game = ClientGameSingleton.getGame();
        try {
            PlayerEntity player = game.getPlayerByName(split[1]);
            player.addCards(Integer.parseInt(split[2]));
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleTakeCards() {
        ClientGame game = ClientGameSingleton.getGame();
        PlayerEntity player = game.getDefender();

        int count = game.getCardsOnTableCount();
        player.addCardsCount(count);

        handleEndMove();
        game.next();
    }

    public static void handlePlayerWonTheGame(String[] split) {
        ClientGame game = ClientGameSingleton.getGame();
        try {
            PlayerEntity player = game.getPlayerByName(split[1]);

            if (player.getName().equals(ClientSingleton.getClient().getName())) {
                Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.ALERT_LABEL);
                label.setText("CONGRATULATIONS!!!! YOU WON THE GAME!!!");
            } else {
                setAlert("CONGRATULATIONS!!!! %S WON THE GAME!!!".formatted(player.getName()));
            }

            game.removePlayer(player);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleQuitGame(String[] split) {
        String name = split[1];
        setAlert("%S left the game".formatted(name));
        ClientGame game = ClientGameSingleton.getGame();

        try {
            PlayerEntity player = game.getPlayerByName(name);
            game.removePlayer(player);

            addCrossOnPlayer(player);

        } catch (PlayerNotFoundException e) {
            // remove from game earlier because of the win
            return;
        }
    }

    private static void addCrossOnPlayer(PlayerEntity player) {
        AnchorPane playerPane = player.getPane();
        Parent parent = playerPane.getParent();

        double leftUpperPointX = playerPane.getLayoutX() + parent.getLayoutX();
        double leftUpperPointY = playerPane.getLayoutY() + parent.getLayoutY();

        Line line1 = new Line();
        line1.setStroke(Paint.valueOf("#ce1515"));
        line1.setStrokeWidth(4);

        line1.setStartX(leftUpperPointX);
        line1.setEndX(leftUpperPointX + playerPane.getWidth());

        line1.setStartY(leftUpperPointY);
        line1.setEndY(leftUpperPointY + playerPane.getHeight());

        Line line2 = new Line();
        line2.setStroke(Paint.valueOf("#ce1515"));
        line2.setStrokeWidth(4);

        line2.setStartX(leftUpperPointX + playerPane.getWidth());
        line2.setEndX(leftUpperPointX);

        line2.setStartY(leftUpperPointY);
        line2.setEndY(leftUpperPointY + playerPane.getHeight());

        AnchorPane mainPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.MAIN_PANE_ID);
        mainPane.getChildren().addAll(line1, line2);
    }

    public static void handleFool(String[] split) {
        String name = split[1];
        Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.ALERT_LABEL);
        setLabel(label, new EndOfMoveTask(), "%s is FOOOOOOOOL!!!".formatted(name));
        ClientGameSingleton.clear();
    }

    public static void handleDraw() {
        Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.ALERT_LABEL);
        setLabel(label, new EndOfMoveTask(), "DRAW!");
        ClientGameSingleton.clear();
    }
}
