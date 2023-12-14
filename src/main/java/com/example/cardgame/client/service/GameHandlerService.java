package com.example.cardgame.client.service;

import com.example.cardgame.client.Client;
import com.example.cardgame.client.ClientSingleton;
import com.example.cardgame.client.ClientGameSingleton;
import com.example.cardgame.client.game.CardEntity;
import com.example.cardgame.client.game.ClientGame;
import com.example.cardgame.client.game.PlayerEntity;
import com.example.cardgame.client.request.generator.ClientGameRequestGenerator;
import com.example.cardgame.client.timerTask.AlertRemovingTask;
import com.example.cardgame.gameProperties.cards.Card;
import com.example.cardgame.client.FxmlObjectProperties;
import com.example.cardgame.properties.ServerProperties;
import com.example.cardgame.server.exception.PlayerNotFoundException;
import com.example.cardgame.server.game.Player;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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
        ClientGameSingleton.getGame().setCanMove(true);
        Label label = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.informationLabelId);
        label.setText("YOUR MOVE");
    }

//    public static void cardIsNotSelectedExceptionHandler() {
//        Label alertLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.alertLabelId);
//        alertLabel.setText("CARD NOT SELECTED");
//        Timer timer = new Timer();
//        timer.schedule(new AlertRemovingTask(alertLabel), 5000);
//    }

    public static void handleNotYourMove() {
    }

    public static void setAlert(Label alertLabel, String text) {
        alertLabel.setText(text);
        Timer timer = new Timer();
        timer.schedule(new AlertRemovingTask(alertLabel), 5000);
    }

    public static boolean canAddCardOnTable(Label alertLabel) {
        ClientGame game = ClientGameSingleton.getGame();
        if (game.getSelectedCard() == null) {
            setAlert(alertLabel, "CARD NOT SELECTED");
            return false;
        }


        if (!game.canAddCardOnTable()) {
            setAlert(alertLabel, "NOT YOUR MOVE YET");
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

        player.reduceCardsCount();

        ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);

        imageView.setOnMouseClicked(mouseEvent -> {
            Label alertLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.alertLabelId);
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
                } else {
                    // TODO: сделать так, чтобы onClick выполнялся только над изображением карты
                    //  (над anchorpane нужно убрать)
                    System.out.println("Alert: \"THIS CARD CANNOT BEAT THE SELECTED CARD\"");
                    setAlert(alertLabel, "THIS CARD CANNOT BEAT THE SELECTED CARD");
                }
            }
        });

        AnchorPane table = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
        CardEntity newCard = new CardEntity(imageView, card);
        addNewCardOnTable(newCard, table);
        ClientGameSingleton.getGame().addCardOnTable(newCard);
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
        imageView.setX(pos * game.getCardsOnTable().size());
        imageView.setLayoutY(0);

        table.getChildren().add(imageView);
    }

    public static void setPlayersOrder(String players) {
        String[] split = players.split(ServerProperties.getSideDelimiter());
        ClientGame game = ClientGameSingleton.getGame();

        List<PlayerEntity> playerEntities = new ArrayList<>();

        // TODO: вывод в javafx

        List<String> playersList = new ArrayList<>(List.of(split));

        AnchorPane pane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.playersListPaneId);

        double width = pane.getWidth();
        double personalWidth = width / (playersList.size());
        int cnt = 1;
        for (String name : playersList) {
            PlayerEntity player = new PlayerEntity(name);
            player.setCardsCount(6);
            player.setImageName("avatar_01.png");

            if (!name.equals(ClientSingleton.getClient().getName())) {
                AnchorPane personalPane = FxmlObjectsGetter.createAnchorPaneForUser(player);
                personalPane.setLayoutY(6);
                personalPane.setLayoutX(cnt * personalWidth - FxmlObjectProperties.anchorPaneForUserWidth / 2);
                pane.getChildren().add(personalPane);
                cnt++;
            }

            playerEntities.add(player);
        }
        game.setPlayers(playerEntities);
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
            AnchorPane buttonPane = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.buttonPositionId);
            Button button = new Button("BEAT");
            button.setOnAction(actionEvent -> {
                Button btn = (Button) actionEvent.getSource();
                AnchorPane parent = (AnchorPane) btn.getParent();
                parent.getChildren().remove(btn);

                game.next();
                Client client = ClientSingleton.getClient();
                client.write(
                        ClientGameRequestGenerator.endMove()
                );
            });
            buttonPane.getChildren().add(button);
        }
    }

    public static void handleAddNewCards(String[] split) {
        ClientGame game = ClientGameSingleton.getGame();
        for (int i = 1; i < split.length; i++) {
            Card card = Card.getCard(split[i]);
            ImageView imageView = FxmlObjectsGetter.createImageViewForCard(card);
            game.addCardOnHand(card, imageView);
        }
        updateCardsOnHands();
    }

    public static void handleEndMove() {
        AnchorPane table = FxmlObjectsGetter.getAnchorPaneById(FxmlObjectProperties.tableCardsPaneId);
        table.getChildren().clear();
        ClientGame game = ClientGameSingleton.getGame();
        game.getCardsOnTable().clear();
        game.setCanMove(false);

        Label informationLabel = FxmlObjectsGetter.getLabelById(FxmlObjectProperties.informationLabelId);
        informationLabel.setText(null);
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
