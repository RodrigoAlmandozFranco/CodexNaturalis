package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.cards.types.playables.StartingCard;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.enumeration.State;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.view.gameview.GameView;
import it.polimi.ingsw.am42.view.gameview.PlayerView;
import it.polimi.ingsw.am42.view.gui.utils.points.ScreenPosition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;
import java.util.List;

public class BoardController implements Initializable {

    private Client client;
    private GameView gameView;
    private PlayerView myPlayer;
    private List<PlayerView> players;
    private List<String> nicknames;
    private final DropShadow highlightEffect = new DropShadow(20, Color.YELLOW);
    //Chat labels
    @FXML
    ListView<TextFlow> listView;
    @FXML
    ScrollPane scrollPane;
    @FXML
    ChoiceBox<String> choiceBox;
    @FXML
    HBox hbox;
    @FXML
    Button button;
    @FXML
    TextField textField;
    //Chat labels end

    //Pickable Cards Labels
    List<Button> pickableCardsButton;
    List<ImageView> pickableResourceCards;
    List<ImageView> pickableGoldCards;
    @FXML
    Label personalNickname;
    @FXML
    ImageView firstCardResource, firstCardGold, pickableResource1, pickableResource2, pickableGold1, pickableGold2;
    @FXML
    Button firstCardResourceButton, pickableResource1Button, pickableResource2Button;
    @FXML
    Button firstCardGoldButton, pickableGold1Button, pickableGold2Button;
    //Pickable cards labels end

    //hand cards
    List<ImageView> hand;
    List<Button> handAndControlButtons;
    PlayableCard chosenCard;
    @FXML
    ImageView handCard1, handCard2, handCard3;
    @FXML
    Button handCard1Button, handCard2Button, handCard3Button;
    @FXML
    Button placeFrontButton, seeFrontButton, placeBackButton, seeBackButton;
    //hand cards end

    Face chosenFace;
    Position chosenPosition;

    //goal cards
    List<ImageView> globalGoals;
    @FXML
    ImageView personalGoal, globalGoal1, globalGoal2;
    //end goal cards

    @FXML
    Label updateText;

    @FXML
    Pane colorPane;
    @FXML
    ChoiceBox<String> colorChoiceBox;
    @FXML
    Button chooseColorButton;
    List<PlayersColor> availableColors;

    @FXML
    Pane goalPane;
    @FXML
    ImageView possibleGoal1, possibleGoal2;
    @FXML
    Button chooseGoalButton, personalGoal1Button, personalGoal2Button;

    @FXML
    Button seeStandingsButton;
    @FXML
    ImageView tokenPlayer1, tokenPlayer2, tokenPlayer3, tokenPlayer4;
    @FXML
    List<ImageView> listTokens;

    @FXML
    Pane standingPane;

    @FXML
    ImageView startingCard;
    @FXML
    Pane boardPane;

    @FXML
    Button pickButton;

    GoalCard chosenGoal;
    int constOffsetX = 97;
    int constOffsetY = 49;

    List<Button> availablePositionsButtons;
    Button chosenPositionButton;

    PlayableCard chosenCardToPick = null;
    List<ImageView> pickableCardsImages;

    List<GoalCard> possibleGoals;

    String currentPlayer;
    String text = "";


    public BoardController() {
    }

    //Start setup BoardController

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        choiceBox.setValue("All");


        pickableCardsButton = new ArrayList<>();
        pickableCardsButton.add(firstCardResourceButton);
        pickableCardsButton.add(pickableResource1Button);
        pickableCardsButton.add(pickableResource2Button);
        pickableCardsButton.add(firstCardGoldButton);
        pickableCardsButton.add(pickableGold1Button);
        pickableCardsButton.add(pickableGold2Button);
        pickableCardsButton.add(pickButton);

        pickableGoldCards = new ArrayList<>();
        pickableGoldCards.add(pickableGold1);
        pickableGoldCards.add(pickableGold2);

        pickableResourceCards = new ArrayList<>();
        pickableResourceCards.add(pickableResource1);
        pickableResourceCards.add(pickableResource2);

        globalGoals = new ArrayList<>();
        globalGoals.add(globalGoal1);
        globalGoals.add(globalGoal2);

        hand = new ArrayList<>();
        hand.add(handCard1);
        hand.add(handCard2);
        hand.add(handCard3);

        handAndControlButtons = new ArrayList<>();
        handAndControlButtons.add(handCard1Button);
        handAndControlButtons.add(handCard2Button);
        handAndControlButtons.add(handCard3Button);
        handAndControlButtons.add(placeFrontButton);
        handAndControlButtons.add(placeBackButton);
        handAndControlButtons.add(seeFrontButton);
        handAndControlButtons.add(seeBackButton);

        listTokens = new ArrayList<>();
        listTokens.add(tokenPlayer1);
        listTokens.add(tokenPlayer2);
        listTokens.add(tokenPlayer3);
        listTokens.add(tokenPlayer4);

        pickableCardsImages = new ArrayList<>();
        pickableCardsImages.add(firstCardResource);
        pickableCardsImages.add(pickableResource1);
        pickableCardsImages.add(pickableResource2);
        pickableCardsImages.add(firstCardGold);
        pickableCardsImages.add(pickableGold1);
        pickableCardsImages.add(pickableGold2);


        firstCardResourceButton.setOnAction(this::firstCardResourceButtonEvent);
        firstCardGoldButton.setOnAction(this::firstCardGoldButtonEvent);
        pickableResource1Button.setOnAction(this::pickableResource1ButtonEvent);
        pickableResource2Button.setOnAction(this::pickableResource2ButtonEvent);
        pickableGold1Button.setOnAction(this::pickableGold1ButtonEvent);
        pickableGold2Button.setOnAction(this::pickableGold2ButtonEvent);
        handCard1Button.setOnAction(this::handCard1ButtonAction);
        handCard2Button.setOnAction(this::handCard2ButtonAction);
        handCard3Button.setOnAction(this::handCard3ButtonAction);
        placeFrontButton.setOnAction(this::placeFrontButtonAction);
        placeBackButton.setOnAction(this::placeBackButtonAction);
        seeFrontButton.setOnAction(this::seeFrontButtonAction);
        seeBackButton.setOnAction(this::seeBackButtonAction);
        chooseColorButton.setOnAction(this::chooseColorButtonAction);
        chooseGoalButton.setOnAction(this::chooseGoalButtonAction);
        personalGoal1Button.setOnAction(this::personalGoal1ButtonAction);
        personalGoal2Button.setOnAction(this::personalGoal2ButtonAction);
        button.setOnAction(this::sendMessage);
        seeStandingsButton.setOnAction(this::seeStandingsButtonAction);

        seeStandingsButton.setOnMouseEntered(event -> seeStandingsButton.setCursor(Cursor.HAND));
        seeStandingsButton.setOnMouseExited(event -> seeStandingsButton.setCursor(Cursor.DEFAULT));


    }

    public void setClient(Client client) {
        this.client = client;
        gameView = client.getView();
        players = gameView.getPlayers();
        nicknames = new ArrayList<>();
        nicknames.add("All");

        for (PlayerView p : players) {
            if (!p.getNickname().equals(gameView.getNickname()))
                nicknames.add(p.getNickname());
            else
                myPlayer = p;
        }
        Platform.runLater(() -> {
            personalNickname.setText(myPlayer.getNickname());
            personalNickname.setStyle("-fx-background-color: #FFEBCD; -fx-text-fill: #DC143C; -fx-padding: 10;");
        });

        Thread thread = new Thread(this::seeMessages);
        thread.start();
        Thread threadGame = new Thread(this::updateGameView);
        threadGame.start();
        this.start();
    }

    public void start() {

        Platform.runLater(() -> {
            choiceBox.getItems().clear();
            choiceBox.getItems().addAll(nicknames);
            choiceBox.setValue("All");
        });

        List<GoalCard> goals = gameView.getGlobalGoals();
        for (int i = 0; i < goals.size(); i++) {
            String src = goals.get(i).getSrcImage();
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(src)));
            globalGoals.get(i).setImage(image);
        }
        availablePositionsButtons = new ArrayList<>();
    }

    //End setup BoardController

    //Start Chat system

    public void seeMessages() {

        while (true) {
            List<ChatMessage> newMessage = gameView.getTmpMessages();
            if (newMessage != null && !newMessage.isEmpty()) {
                Platform.runLater(() -> {
                    updateListView(newMessage);
                });
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private void updateListView(List<ChatMessage> newMessage) {
        for (ChatMessage message : newMessage) {
            String sender;
            if (gameView.getNickname().equals(message.getSender()))
                sender = "You";
            else
                sender = message.getSender();

            TextFlow textFlow = new TextFlow();
            textFlow.setPrefWidth(listView.getWidth() - 20);
            textFlow.setLineSpacing(5.0);

            Text messageText = new Text(sender + " to " + message.getReceiver() + ": " + message.getMessage());
            messageText.setStyle("-fx-font-weight: bold;");

            textFlow.getChildren().add(messageText);

            listView.getItems().add(textFlow);
        }
        Platform.runLater(() -> listView.scrollTo(listView.getItems().size() - 1));
    }

    public void sendMessage(ActionEvent e) {
        String message = textField.getText();
        String sender = gameView.getNickname();
        String receiver = choiceBox.getValue();

        if (message.isEmpty()) return;

        textField.clear();

        choiceBox.setValue("All");

        ChatMessage chatMessage;

        if (receiver.equals("All"))
            chatMessage = new ChatMessage(message, sender);
        else
            chatMessage = new ChatMessage(message, sender, receiver);
        client.sendChatMessage(chatMessage);
    }

    //End Chat system


    //Start Button control system

    private void enablePickableButtons() {
        for (Button b : pickableCardsButton) {
            b.setDisable(false);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.HAND));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
        pickButton.setOpacity(1);
    }

    private void disablePickableButtons() {
        for (Button b : pickableCardsButton) {
            b.setDisable(true);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.DEFAULT));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
        pickButton.setOpacity(0);
    }

    private void enableHandAndControlButtons() {
        for (Button b : handAndControlButtons) {
            b.setDisable(false);
            if (b.equals(placeFrontButton) || b.equals(placeBackButton) || b.equals(seeFrontButton) || b.equals(seeBackButton))
                b.setOpacity(1);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.HAND));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
    }

    private void disableHandAndControlButtons() {
        for (Button b : handAndControlButtons) {
            b.setDisable(true);
            if (b.equals(placeFrontButton) || b.equals(placeBackButton) || b.equals(seeFrontButton) || b.equals(seeBackButton))
                b.setOpacity(0);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.DEFAULT));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
    }

    //End Button control system

    //Start personal color system

    private void setBackgroundNickname(PlayersColor color) {
        Platform.runLater(() -> {
            switch (color) {
                case RED -> personalNickname.setStyle("-fx-background-color: red; -fx-padding: 10;");
                case GREEN -> personalNickname.setStyle("-fx-background-color: green; -fx-padding: 10;");
                case YELLOW -> personalNickname.setStyle("-fx-background-color: yellow; -fx-padding: 10;");
                case BLUE -> personalNickname.setStyle("-fx-background-color: blue; -fx-padding: 10;");
            }
        });
    }

    private void setColor() {

        List<String> colors = new ArrayList<>();
        for(PlayersColor pc : availableColors){
            switch(pc) {
                case RED -> colors.add("RED");
                case BLUE -> colors.add("BLUE");
                case GREEN -> colors.add("GREEN");
                case YELLOW -> colors.add("YELLOW");
            }
        }


        Platform.runLater(() -> {
            colorChoiceBox.getItems().clear();
            colorChoiceBox.getItems().addAll(colors);
        });
        colorPane.setOpacity(1);
        colorPane.setDisable(false);
    }

    public void chooseColorButtonAction(ActionEvent event) {
        if (colorChoiceBox.getValue() == null) {
            showAlert("You have to choose a color");
            return;
        }

        PlayersColor chosenColor = PlayersColor.valueOf(colorChoiceBox.getValue());

        colorPane.setOpacity(0);
        colorPane.setDisable(true);

        possibleGoals = client.chooseColor(myPlayer.getNickname(), chosenColor);
        myPlayer.setColor(chosenColor);
        setBackgroundNickname(chosenColor);
    }

    //End personal color system


    //Start personal goal system

    public void personalGoal1ButtonAction(ActionEvent event) {
        possibleGoal1.setEffect(highlightEffect);
        possibleGoal2.setEffect(null);
        chosenGoal = possibleGoals.getFirst();
    }

    public void personalGoal2ButtonAction(ActionEvent event) {
        possibleGoal1.setEffect(null);
        possibleGoal2.setEffect(highlightEffect);
        chosenGoal = possibleGoals.get(1);
    }

    public void chooseGoalButtonAction(ActionEvent event) {
        if (chosenGoal == null) {
            showAlert("You have to choose a goal");
            return;
        }

        goalPane.setOpacity(0);
        goalPane.setDisable(true);

        client.chooseGoal(myPlayer.getNickname(), chosenGoal);
        personalGoal.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream(chosenGoal.getSrcImage()))));
    }

    private void setGoal() {

        for (GoalCard card : possibleGoals) {
            if (card.equals(possibleGoals.getFirst())) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getSrcImage())));
                possibleGoal1.setImage(image);
            } else {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(card.getSrcImage())));
                possibleGoal2.setImage(image);
            }
        }

        goalPane.setOpacity(1);
        goalPane.setDisable(false);
    }

    //End personal goal system


    //Start Pickable cards system

    public void pickableResource1ButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        pickableResource1.setEffect(highlightEffect);
        chosenCardToPick = gameView.getPickableResourceCards().getFirst();
        //client.pick(myPlayer.getNickname(), gameView.getPickableResourceCards().getFirst());
    }

    public void pickableGold1ButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        pickableGold1.setEffect(highlightEffect);
        chosenCardToPick = gameView.getPickableGoldCards().getFirst();
        //client.pick(myPlayer.getNickname(), gameView.getPickableGoldCards().getFirst());
    }

    public void pickableResource2ButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        pickableResource2.setEffect(highlightEffect);
        chosenCardToPick = gameView.getPickableResourceCards().get(1);
        //client.pick(myPlayer.getNickname(), gameView.getPickableResourceCards().get(1));
    }

    public void pickableGold2ButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        pickableGold2.setEffect(highlightEffect);
        chosenCardToPick = gameView.getPickableGoldCards().get(1);
        //client.pick(myPlayer.getNickname(), gameView.getPickableGoldCards().get(1));
    }

    public void pickButtonAction(ActionEvent event) {
        if (chosenCardToPick == null)
            showAlert("You have to pick a card!");
        client.pick(myPlayer.getNickname(), chosenCardToPick);
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        chosenCardToPick = null;
    }

    public void firstCardResourceButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        firstCardResource.setEffect(highlightEffect);
        for (PlayableCard p : gameView.getPickableResourceCards()) {
            if (!p.getVisibility()) {
                chosenCardToPick = p;
                break;
            }
        }
        //client.pick(myPlayer.getNickname(), card);
    }

    public void firstCardGoldButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        firstCardGold.setEffect(highlightEffect);
        for (PlayableCard p : gameView.getPickableGoldCards()) {
            if (!p.getVisibility()) {
                chosenCardToPick = p;
                break;
            }
        }
        //client.pick(myPlayer.getNickname(), card);
    }

    //End Pickable cards system


    //Start hand control system

    public void seeFrontButtonAction(ActionEvent event) {
        if (chosenCard == null) {
            showAlert("You have to choose a card");
            return;
        }

        if (chosenCard instanceof StartingCard) {
            seeBackStartingCard();
        } else {
            modifyHandCardImage(chosenCard.getFront().getSrcImage());
        }
    }

    public void seeBackButtonAction(ActionEvent event) {
        if (chosenCard == null) {
            showAlert("You have to choose a card");
            return;
        }
        if (chosenCard instanceof StartingCard) {
            seeFrontStartingCard();
        } else {
            modifyHandCardImage(chosenCard.getBack().getSrcImage());
        }
    }

    private void seeBackStartingCard() {
        modifyHandCardImage(chosenCard.getBack().getSrcImage());
    }

    private void seeFrontStartingCard() {
        modifyHandCardImage(chosenCard.getFront().getSrcImage());
    }

    private void modifyHandCardImage(String src) {
        new Thread(() -> {
            try {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(src)));
                Platform.runLater(() -> {
                    hand.get(myPlayer.getHand().indexOf(chosenCard)).setImage(image);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void handCard1ButtonAction(ActionEvent event) {
        chosenCard = myPlayer.getHand().getFirst();
        highlightHandCard(handCard1);
    }

    public void handCard2ButtonAction(ActionEvent event) {
        chosenCard = myPlayer.getHand().get(1);
        highlightHandCard(handCard2);
    }

    public void handCard3ButtonAction(ActionEvent event) {
        chosenCard = myPlayer.getHand().get(2);
        highlightHandCard(handCard3);
    }

    public void placeFrontButtonAction(ActionEvent event) {
        if (checkBeforePlace()) return;

        if (chosenCard instanceof StartingCard) {
            placeStartingCard(chosenCard.getBack());
            return;
        }

        placeCard(chosenCard.getFront());
    }

    public void placeBackButtonAction(ActionEvent event) {
        if (checkBeforePlace()) return;

        if (chosenCard instanceof StartingCard) {
            placeStartingCard(chosenCard.getBack());
            return;
        }

        placeCard(chosenCard.getBack());
    }

    private void placeCard(Face face){
        String value = chosenPositionButton.getText();
        String x = "", y = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ',') {
                x = value.substring(0, i).trim();
                y = value.substring(i + 1).trim();
            }
        }

        Position chosenPosition = new Position(Integer.parseInt(x), Integer.parseInt(y));

        chosenCard = null;

        try {
            client.place(myPlayer.getNickname(), face, chosenPosition);

        } catch (RequirementsNotMetException e) {
            showAlert("The requirements are not met");
            return;
        }
        addFaceToBoard(face);

        for (Button b : availablePositionsButtons) {
            boardPane.getChildren().remove(b);
            b.setEffect(null);
        }
    }

    private void placeStartingCard(Face face) {
        availableColors = client.placeStarting(myPlayer.getNickname(), face);
        handCard1.setEffect(null);
        handCard2.setEffect(null);
        handCard3.setEffect(null);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(face.getSrcImage())));
        startingCard.setImage(image);
        chosenCard = null;
    }

    private boolean checkBeforePlace() {
        if (chosenCard == null) {
            showAlert("You have to choose a card");
            return true;
        }
        if (chosenPositionButton == null && !(chosenCard instanceof StartingCard)) {
            showAlert("You have to choose a position");
            return true;
        }
        return false;
    }

    private void highlightHandCard(ImageView cardImageView) {
        for (ImageView handCard : hand) {
            handCard.setEffect(null);
        }
        cardImageView.setEffect(highlightEffect);
    }

    //End hand control system


    //Start board control system

    private void setupTurn() {
        Set<Position> availablePositions = client.getAvailablePositions(myPlayer.getNickname());
        for (Position position : availablePositions) {
            double offsetX = startingCard.getLayoutX();
            double offsetY = startingCard.getLayoutY();
            int x = position.getX();
            int y = position.getY();

            while (x > 0) {
                offsetX += constOffsetX;
                offsetY -= constOffsetY;
                x--;
            }
            while (y > 0) {
                offsetY -= constOffsetY;
                offsetX -= constOffsetX;
                y--;
            }
            while (x < 0) {
                offsetX -= constOffsetX;
                offsetY += constOffsetY;
                x++;
            }

            while (y < 0) {
                offsetY += constOffsetY;
                offsetX += constOffsetX;
                y++;
            }


            double finalOffsetX = offsetX;
            double finalOffsetY = offsetY;


            double finalWidth = boardPane.getChildren().getFirst().getLayoutBounds().getWidth();
            double finalHeight = boardPane.getChildren().getFirst().getLayoutBounds().getHeight();


            Platform.runLater(() -> {
                Button button = new Button(position.getX() + ", " + position.getY());
                boardPane.getChildren().add(button);
                button.setPrefWidth(finalWidth);
                button.setPrefHeight(finalHeight);
                button.setLayoutX(finalOffsetX);
                button.setLayoutY(finalOffsetY);
                button.setOpacity(0.5);
                availablePositionsButtons.add(button);

                button.setOnMouseEntered(event -> button.setCursor(Cursor.HAND));
                button.setOnMouseExited(event -> button.setCursor(Cursor.DEFAULT));

                button.setOnAction(event -> {
                    for (Button b : availablePositionsButtons) {
                        b.setEffect(null);
                    }
                    chosenPositionButton = button;
                    button.setEffect(highlightEffect);
                });
            });

        }
    }

    private void addFaceToBoard(Face face) {
        handCard1.setEffect(null);
        handCard2.setEffect(null);
        handCard3.setEffect(null);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(face.getSrcImage())));
        ImageView card = new ImageView(image);
        card.setFitHeight(chosenPositionButton.getPrefHeight());
        card.setFitWidth(chosenPositionButton.getPrefWidth());
        card.setLayoutX(chosenPositionButton.getLayoutX());
        card.setLayoutY(chosenPositionButton.getLayoutY());
        chosenPositionButton = null;
        for (Button b : availablePositionsButtons) {
            boardPane.getChildren().remove(b);
            b.setEffect(null);
        }
        availablePositionsButtons.clear();
        boardPane.getChildren().add(card);
    }

    private void resizeBoardPane() {

    }

    //End board control system

    //Start utils

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void seeStandingsButtonAction(ActionEvent event) {
        seeStandingsButton.setDisable(true);
        List<PlayerView> players = gameView.getPlayers();
        for (PlayerView p : players) {
            ScreenPosition screenPosition = new ScreenPosition();
            if (p.getColor() != null) {
                Image image = null;
                switch (p.getColor()) {
                    case RED ->
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/red.png")));
                    case BLUE ->
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/blue.png")));
                    case YELLOW ->
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/yellow.png")));
                    case GREEN ->
                            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/green.png")));
                }

                ScreenPosition coords = screenPosition.getScreenPosition(players.indexOf(p), p.getPoints());
                listTokens.get(players.indexOf(p)).setX(coords.getX());
                listTokens.get(players.indexOf(p)).setY(coords.getY());

                Image finalImage = image;
                Platform.runLater(() -> {
                    listTokens.get(players.indexOf(p)).setImage(finalImage);
                });
            }
        }

        for (ImageView token : listTokens) {
            token.setOpacity(1);
            token.setDisable(false);
        }


        standingPane.setOpacity(1);
        standingPane.setDisable(false);
        standingPane.setVisible(true);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> {
                    for (ImageView token : listTokens) {
                        token.setOpacity(0);
                        token.setDisable(true);
                    }
                    standingPane.setOpacity(0);
                    standingPane.setDisable(true);
                    standingPane.setVisible(false);
                    seeStandingsButton.setDisable(false);
                }
        ));
        timeline.setCycleCount(1);
        timeline.play();


    }

    //End utils


    //Start update Game

    private void disableAll() {
        disableHandAndControlButtons();
        disablePickableButtons();
        colorPane.setOpacity(0);
        colorPane.setDisable(true);
        goalPane.setOpacity(0);
        goalPane.setDisable(true);
    }

    private void updateStateSetHand(){

        text = "";
        currentPlayer = "";

        if (gameView.getCurrentPlayer().equals(myPlayer)) {
            text += "It's your turn! You have to place your starting card";
            enableHandAndControlButtons();
            disablePickableButtons();
        } else {
            currentPlayer = gameView.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to place the starting card";
            disableAll();
        }
    }


    private void updateStateSetColor(){
        text = "";
        currentPlayer = "";

        if (gameView.getCurrentPlayer().equals(myPlayer)) {
            text += "It's your turn! You have to choose your personal color";
            disableHandAndControlButtons();
            disablePickableButtons();
            setColor();
        } else {
            currentPlayer = gameView.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to choose the personal color";
            disableAll();
        }
    }

    private void updateStateSetGoal() {
        text = "";
        currentPlayer = "";

        if (gameView.getCurrentPlayer().equals(myPlayer)) {
            text += "It's your turn! You have to choose your personal goal";
            while (possibleGoals == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            disableHandAndControlButtons();
            disablePickableButtons();
            setGoal();
        } else {
            currentPlayer = gameView.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to choose the personal goal";
            disableAll();
        }
    }

    private void updateStatePlace() {
        text = "";
        currentPlayer = "";

        if (gameView.getCurrentPlayer().equals(myPlayer)) {
            text += "It's your turn! You have to place a card";
            boardPane.setOpacity(1);
            boardPane.setDisable(false);
            setupTurn();
            enableHandAndControlButtons();
        } else {
            currentPlayer = gameView.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to place a card";
            disableAll();
        }
    }

    private void updateStatePick() {
        text = "";
        currentPlayer = "";

        seeStandingsButtonAction(null);

        if (gameView.getCurrentPlayer().equals(myPlayer)) {
            text += "It's your turn! You have to pick a card";
            enablePickableButtons();
            disableHandAndControlButtons();
        } else {
            currentPlayer = gameView.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to pick a card";
            disableAll();
        }
    }

    private void updateStateLast() {
        //todo
    }

    private void updateStateDisconnected() {
        disableAll();
        showAlert("A player has disconnected from the game.");
        showAlert("The game will be closed.");
        System.exit(1);
    }

    private void updateHandMyPlayer() {
        List<PlayableCard> hand = myPlayer.getHand();
        for (int i = 0; i < this.hand.size(); i++)
            this.hand.get(i).setImage(null);

        for (int i = 0; i < hand.size(); i++) {
            String src = hand.get(i).getFront().getSrcImage();
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(src)));
            this.hand.get(i).setImage(image);
        }
    }

    private void updatePickableCards() {
        List<PlayableCard> pickableResources = gameView.getPickableResourceCards();
        List<PlayableCard> pickableGold = gameView.getPickableGoldCards();

        List<List<PlayableCard>> pickables = new ArrayList<>();
        pickables.add(pickableResources);
        pickables.add(pickableGold);

        for (List<PlayableCard> pickable : pickables) {
            ImageView firstCard;
            List<ImageView> pickableCards;
            if (pickable.getFirst() instanceof ResourceCard) {
                firstCard = firstCardResource;
                pickableCards = pickableResourceCards;
            } else {
                firstCard = firstCardGold;
                pickableCards = pickableGoldCards;
            }
            for (PlayableCard p : pickable) {
                if (!p.getVisibility()) {
                    String src = p.getBack().getSrcImage();
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(src)));
                    firstCard.setImage(image);
                } else {
                    String src = p.getFront().getSrcImage();
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(src)));
                    pickableCards.get(pickable.indexOf(p)).setImage(image);
                }
            }
        }
    }


    public void updateGameView() {
        while (true) {
            if (gameView.getNewUpdate()) {

                if (gameView.getCurrentState().equals(State.SETHAND)) {
                   updateStateSetHand();
                } else if (gameView.getCurrentState().toString().equals("SETCOLOR")) {
                    updateStateSetColor();
                } else if (gameView.getCurrentState().toString().equals("SETGOAL")) {
                    updateStateSetGoal();
                } else if (gameView.getCurrentState().toString().equals("PLACE")) {
                    updateStatePlace();
                } else if (gameView.getCurrentState().toString().equals("PICK")) {
                    updateStatePick();
                } else if (gameView.getCurrentState().toString().equals("LAST")) {
                    updateStateLast();
                } else {
                    updateStateDisconnected();
                }

                updateHandMyPlayer();

                updatePickableCards();


                //todo points + boards


                String finalText = text;
                Platform.runLater(() -> {
                    updateText.setStyle("-fx-background-color: #FFEBCD; -fx-text-fill: #DC143C; -fx-padding: 10;");
                    updateText.setWrapText(true);
                    updateText.setTextAlignment(TextAlignment.LEFT);
                    updateText.setText(finalText);
                });
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
