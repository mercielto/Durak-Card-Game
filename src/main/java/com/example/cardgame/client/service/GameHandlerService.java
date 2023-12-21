package com.example.cardgame.client.service;

import com.example.cardgame.client.*;
import com.example.cardgame.client.game.CardEntity;
import com.example.cardgame.client.game.ClientGame;
import com.example.cardgame.client.game.PlayerEntity;
import com.example.cardgame.client.request.generator.ClientGameRequestGenerator;
import com.example.cardgame.client.timerTask.AlertRemovingTask;
import com.example.cardgame.client.timerTask.EndOfMoveTask;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.server.exception.PlayerNotFoundException;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
        ClientGame game = ClientGameSingleton.getGame();
        game.setTrumpCard(trumpCard, trumpCardPlace);

        resetCardsOnHands(cards);

//        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.handCardsAnchorPaneId);
//        double size = pane.getWidth();
//        double spacing = size / cards.size();
//        for (int i = 0; i < cards.size(); i++) {
//            Card card = cards.get(i);
//            ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);
//            imageView.setX(spacing * i);
//
//            imageView.setOnMouseClicked(mouseEvent -> {
//                ImageView img = (ImageView) mouseEvent.getSource();
//
//                CardEntity cardEntity = game.getSelectedCard();
//                if (cardEntity != null) {
//                    cardEntity.getImg().setLayoutY(0);
//                }
//
//                img.setLayoutY(-30);
//
//                game.setSelectedCard(img);
//            });
//
//            game.addCardOnHand(card, imageView);
//
//            pane.getChildren().add(imageView);
//        }
    }

    public static void updateCardsOnHands() {
        ClientGame game = ClientGameSingleton.getGame();
        resetCardsOnHands(new ArrayList<>(game.getCardsOnHands()));
    }

    public static void resetCardsOnHands(List<Card> cards) {
        ClientGame game = ClientGameSingleton.getGame();
        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.handCardsAnchorPaneId);
        pane.getChildren().clear();
        game.getCardsOnHands().clear();
        double size = pane.getWidth();
        double spacing = size / cards.size();
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);
            imageView.setX(spacing * i);

            imageView.setOnMouseClicked(mouseEvent -> {
                ImageView img = (ImageView) mouseEvent.getSource();

                CardEntity cardEntity = game.getSelectedCard();
                if (cardEntity != null) {
                    cardEntity.getImg().setLayoutY(0);
                }

                img.setLayoutY(-30);

                game.setSelectedCard(img);
            });

            game.addCardOnHand(card, imageView);

            pane.getChildren().add(imageView);
        }
    }

    public static void handleYourMove() {
        ClientGame game = ClientGameSingleton.getGame();
        game.setCanMove(true);

        Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.informationLabelId);
        label.setText("YOUR MOVE");

        if (game.getCardsOnTable().size() != 0) {
            addBeatButton();
        }

    }

//    public static void cardIsNotSelectedExceptionHandler() {
//        Label alertLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.alertLabelId);
//        alertLabel.setText("CARD NOT SELECTED");
//        Timer timer = new Timer();
//        timer.schedule(new AlertRemovingTask(alertLabel), 5000);
//    }

    public static void handleNotYourMove() {
    }

    public static void setLabel(Label alertLabel, TimerTask task, String text) {
        alertLabel.setText(text);
        Timer timer = new Timer();
        timer.schedule(task, 5000);
    }

    public static void setAlert(String text) {
        Label alertLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.alertLabelId);
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

//        imageView.setLayoutX(player.getPane().getLayoutX());
//        imageView.setLayoutY(player.getPane().getLayoutY());

        imageView.setOnMouseClicked(mouseEvent -> {
            AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
            
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
                    // TODO: сделать так, чтобы onClick выполнялся только над изображением карты
                    //  (над anchorpane нужно убрать)
                    System.out.println("Alert: \"THIS CARD CANNOT BEAT THE SELECTED CARD\"");
                    setAlert("THIS CARD CANNOT BEAT THE SELECTED CARD");
                }
            }
        });

        AnchorPane table = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
        CardEntity newCard = new CardEntity(imageView, card);
        addNewCardOnTable(newCard, table);
        ClientGameSingleton.getGame().addCardOnTable(newCard);
    }

    public static void removeButtonsFromPositionPane() {
        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.buttonPositionId);
        pane.getChildren().clear();
    }
    
    public static void setTranslationOfCard(Node node, double setByX, double setByY) {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(5000));
        translateTransition.setNode(node);
        translateTransition.setByX(setByX);
        translateTransition.setByY(setByY);
        translateTransition.setAutoReverse(false);
        translateTransition.play();
    }

    public static void addTakeCardsButton() {
        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.buttonPositionId);
        ObservableList<Node> children = pane.getChildren();

        Button button = new Button("Take cards");
        button.setOnAction(actionEvent -> {
            ClientGame game = ClientGameSingleton.getGame();
            game.takeTableCardsOnHands();
            game.next();
            game.next();
            children.clear();
            ClientSingleton.getClient().write(
                    ClientGameRequestGenerator.takeCards()
            );
            updateCardsOnHands();
            clearCardsOnTable();
        });

        children.clear();
        children.add(button);
    }

    public static void setBeatCardPosition(ImageView img, AnchorPane pane, CardEntity cardToBeat) {
        img.setX(cardToBeat.getImg().getX());
        img.setY(cardToBeat.getImg().getY());
        img.setLayoutY(10);
        img.setLayoutX(10);
        img.setOnMouseClicked(null);

        pane.getChildren().add(img);
        cardToBeat.getImg().setOnMouseClicked(null);
    }

    public static void addNewCardOnTable(CardEntity cardEntity, AnchorPane table) {
        ImageView imageView = cardEntity.getImg();
        ClientGame game = ClientGameSingleton.getGame();

        double pos = table.getWidth() / 6;

//        double setX = pos * game.getCardsOnTable().size();
//        double setY = 0;
//
//        setTranslationOfCard(imageView, imageView.getX() - setX, imageView.getY() - setY);
        imageView.setX(pos * game.getCardsOnTable().size());
        imageView.setLayoutY(0);

        table.getChildren().add(imageView);
    }

    public static void setPlayersOrder(String players) {
        String[] split = players.split(ServerProperties.getSideDelimiter());
        ClientGame game = ClientGameSingleton.getGame();

        List<PlayerEntity> playerEntities = new ArrayList<>();

        List<String> playersList = new ArrayList<>(List.of(split));

        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.playersListPaneId);

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
                personalPane.setLayoutX(cnt * personalWidth - FxmlObjectProperties.anchorPaneForUserWidth / 2);
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

        // TODO: СДЕЛАТЬ ТАК, ЧТОБЫ КАРТЫ ВЫХОДИЛИ ОТ ИГРОКА ПО ИМЕНИ NAME

        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
        setBeatCardPosition(cardImage, pane, cardToBeatEntity);


        if (game.canAddCardOnTable()) {
            addBeatButton();
        }
    }

    private static void addBeatButton() {
        AnchorPane buttonPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.buttonPositionId);
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
        for (int i = 1; i < split.length; i++) {
            Card card = Card.getCard(split[i]);
            ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);
            game.addCardOnHand(card, imageView);
        }

        if (game.getCardsOnHands().size() == 0) {
            ClientSingleton.getClient().write(
                    ClientGameRequestGenerator.noMoreCardsOnHands()
            );
        }

        updateCardsOnHands();
    }

    public static void handleEndMove() {
        AnchorPane table = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
        table.getChildren().clear();
        ClientGame game = ClientGameSingleton.getGame();
        game.getCardsOnTable().clear();
        game.setCanMove(false);
        game.removePlayerBorders();
        game.next();

        Label informationLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.informationLabelId);
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

    private static void clearCardsOnTable() {
        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
        pane.getChildren().clear();
    }

    public static void handlePlayerWonTheGame(String[] split) {
        ClientGame game = ClientGameSingleton.getGame();
        try {
            PlayerEntity player = game.getPlayerByName(split[1]);

            // TODO: анимация выигрыша

            // temporary notifications
            if (player.getName().equals(ClientSingleton.getClient().getName())) {
                Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.alertLabelId);
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

            // TODO: добавить большой крест над игроком, в знак того, что он покинул игру
        } catch (PlayerNotFoundException e) {
            return;
        }
    }

    public static void handleFool(String[] split) {
        String name = split[1];
        Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.alertLabelId);
        setLabel(label, new EndOfMoveTask(), "%s is FOOOOOOOOL!!!".formatted(name));
    }

    public static void handleDraw() {
        Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.alertLabelId);
        setLabel(label, new EndOfMoveTask(), "DRAW!");
    }

//    public static void updateTableCards() {
//        ClientGame game = GameSingleton.getGame();
//        AnchorPane table = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
//        int count = 0;
//        for (CardImageView cardImageView : game.getCardsOnTable()) {
//            ImageView img = cardImageView.getImg();
//            double pos = table.getWidth() / (table.getChildren().size() + 1);
//            img.setX(pos * count);
//            count++;
//            table.getChildren().add(img);
//        }
//    }
}
