package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.exceptions.WrongTurnException;
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
import it.polimi.ingsw.am42.view.clientModel.GameClientModel;
import it.polimi.ingsw.am42.view.clientModel.PlayerClientModel;
import it.polimi.ingsw.am42.view.gui.HelloApplication;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import it.polimi.ingsw.am42.view.gui.utils.Coordinates;
import it.polimi.ingsw.am42.view.gui.utils.points.ScreenPosition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class BoardController implements Initializable {

    private Client client;
    private GameClientModel gameClientModel;
    private PlayerClientModel myPlayer;
    private List<PlayerClientModel> players;
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

    @FXML
    ImageView tokenOnStarting;

    GoalCard chosenGoal;
    double constOffsetX = 80.5;
    double constOffsetY = 40.5;

    List<Button> availablePositionsButtons;
    Button chosenPositionButton;

    PlayableCard chosenCardToPick = null;
    List<ImageView> pickableCardsImages;

    List<GoalCard> possibleGoals;

    @FXML
    Button player1Board, player2Board, player3Board;
    List<Button> playersBoardButtons;

    String currentPlayer;
    String text = "";

    @FXML
    Pane boardPaneOtherPlayer;
    @FXML
    Label boardLabelOtherPlayer;

    Thread messagesThread;

    private boolean gameToBeLoad = false;

    /**

     * @author Mattia Brandi
     * @author Rodrigo Almandoz Franco
     */
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

        placedCards = new ArrayList<>();

        playersBoardButtons = new ArrayList<>();
        playersBoardButtons.add(player1Board);
        playersBoardButtons.add(player2Board);
        playersBoardButtons.add(player3Board);

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
        player1Board.setOnAction(this::player1BoardButtonAction);
        player2Board.setOnAction(this::player2BoardButtonAction);
        player3Board.setOnAction(this::player3BoardButtonAction);

        seeStandingsButton.setStyle("-fx-background-color: brown; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");;

        seeStandingsButton.setOnMouseEntered(event -> seeStandingsButton.setCursor(Cursor.HAND));
        seeStandingsButton.setOnMouseExited(event -> seeStandingsButton.setCursor(Cursor.DEFAULT));

        for(Button b : playersBoardButtons){
            b.setOnMouseEntered(event -> b.setCursor(Cursor.HAND));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }

    }

    public void setClient(Client client, boolean gameToBeLoad) {
        this.client = client;
        this.gameToBeLoad = gameToBeLoad;
        gameClientModel = client.getView();
        players = gameClientModel.getPlayers();
        nicknames = new ArrayList<>();
        nicknames.add("All");

        for (PlayerClientModel p : players) {
            if (!p.getNickname().equals(gameClientModel.getNickname()))
                nicknames.add(p.getNickname());
            else
                myPlayer = p;
        }
        Platform.runLater(() -> {
            personalNickname.setText(myPlayer.getNickname());
            personalNickname.setStyle("-fx-background-color: #FFEBCD; -fx-text-fill: #DC143C; -fx-padding: 10;");
        });

        for(Button b : playersBoardButtons){
            b.setDisable(true);
            b.setOpacity(0);
        }

        int i;

        for (i = 1; i < nicknames.size(); i++){
            playersBoardButtons.get(i - 1).setDisable(false);
            playersBoardButtons.get(i - 1).setOpacity(1);
            playersBoardButtons.get(i - 1).setText(nicknames.get(i) + "'s board");
        }

        int size = playersBoardButtons.size();

        int removed = 0;
        for(i = i - 1; i < size; i++) {
            playersBoardButtons.remove(i - removed);
            removed++;
        }

        messagesThread = new Thread(this::seeMessages);
        messagesThread.start();
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

        List<GoalCard> goals = gameClientModel.getGlobalGoals();
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
            List<ChatMessage> newMessage = gameClientModel.getTmpMessages();
            if (newMessage != null && !newMessage.isEmpty()) {
                Platform.runLater(() -> {
                    updateListView(newMessage);
                });
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    private void updateListView(List<ChatMessage> newMessage) {
        for (ChatMessage message : newMessage) {
            String sender;
            if (gameClientModel.getNickname().equals(message.getSender()))
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
        String sender = gameClientModel.getNickname();
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
        enablePickButton();
    }

    private void disablePickableButtons() {
        for (Button b : pickableCardsButton) {
            b.setDisable(true);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.DEFAULT));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
        disablePickButton();
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
        if(color != null) {
            Platform.runLater(() -> {
                switch (color) {
                    case RED -> personalNickname.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
                    case GREEN -> personalNickname.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
                    case YELLOW -> personalNickname.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10;");
                    case BLUE -> personalNickname.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
                }
            });
        }
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
        chooseColorButton.setOnMouseEntered(event -> chooseColorButton.setCursor(Cursor.HAND));
        chooseColorButton.setOnMouseExited(event -> chooseColorButton.setCursor(Cursor.DEFAULT));
    }

    public void chooseColorButtonAction(ActionEvent event) {
        if (colorChoiceBox.getValue() == null) {
            showAlert("You have to choose a color");
            return;
        }

        PlayersColor chosenColor = PlayersColor.valueOf(colorChoiceBox.getValue());

        colorPane.setOpacity(0);
        colorPane.setDisable(true);

        Image image = null;
        switch (chosenColor) {
            case RED ->
                    image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/red.png")));
            case BLUE ->
                    image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/blue.png")));
            case YELLOW ->
                    image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/yellow.png")));
            case GREEN ->
                    image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/green.png")));
        }

        tokenOnStarting.setImage(image);

        try {
            possibleGoals = client.chooseColor(myPlayer.getNickname(), chosenColor);
        } catch (WrongTurnException e){
            showAlert(e.getMessage());
            return;
        }

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

        try {
            client.chooseGoal(myPlayer.getNickname(), chosenGoal);
        } catch (WrongTurnException e){
            showAlert(e.getMessage());
            return;
        }
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
        personalGoal1Button.setOnMouseEntered(event -> personalGoal1Button.setCursor(Cursor.HAND));
        personalGoal1Button.setOnMouseExited(event -> personalGoal1Button.setCursor(Cursor.DEFAULT));
        personalGoal2Button.setOnMouseEntered(event -> personalGoal2Button.setCursor(Cursor.HAND));
        personalGoal2Button.setOnMouseExited(event -> personalGoal2Button.setCursor(Cursor.DEFAULT));
        chooseGoalButton.setOnMouseEntered(event -> chooseGoalButton.setCursor(Cursor.HAND));
        chooseGoalButton.setOnMouseExited(event -> chooseGoalButton.setCursor(Cursor.DEFAULT));
    }

    //End personal goal system


    //Start Pickable cards system

    public void pickableResource1ButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        pickableResource1.setEffect(highlightEffect);
        chosenCardToPick = gameClientModel.getPickableResourceCards().getFirst();
    }

    public void pickableGold1ButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        pickableGold1.setEffect(highlightEffect);
        chosenCardToPick = gameClientModel.getPickableGoldCards().getFirst();
    }

    public void pickableResource2ButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        pickableResource2.setEffect(highlightEffect);
        chosenCardToPick = gameClientModel.getPickableResourceCards().get(1);
    }

    public void pickableGold2ButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        pickableGold2.setEffect(highlightEffect);
        chosenCardToPick = gameClientModel.getPickableGoldCards().get(1);
    }

    public void pickButtonAction(ActionEvent event) {
        if(gameClientModel.getCurrentState().equals(State.PICK)){
            if (chosenCardToPick == null)
                showAlert("You have to pick a card!");

            try {
                client.pick(myPlayer.getNickname(), chosenCardToPick);
            } catch (WrongTurnException e){
                showAlert(e.getMessage());
                return;
            }

            for (ImageView b : pickableCardsImages) {
                b.setEffect(null);
            }
            chosenCardToPick = null;
            Platform.runLater(this::disablePickButton);
        } else {
            Platform.runLater(this::disablePickButton);
        }
    }

    public void firstCardResourceButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        firstCardResource.setEffect(highlightEffect);
        for (PlayableCard p : gameClientModel.getPickableResourceCards()) {
            if (!p.getVisibility()) {
                chosenCardToPick = p;
                break;
            }
        }
    }

    public void firstCardGoldButtonEvent(ActionEvent event) {
        for (ImageView b : pickableCardsImages) {
            b.setEffect(null);
        }
        firstCardGold.setEffect(highlightEffect);
        for (PlayableCard p : gameClientModel.getPickableGoldCards()) {
            if (!p.getVisibility()) {
                chosenCardToPick = p;
                break;
            }
        }
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
            placeStartingCard(chosenCard.getFront());
            return;
        }

        placeCard(chosenCard.getBack());
    }

    /**
     * This method places the card in the Board
     * This method verifies if the requirements of the card are satisfied
     * @param face the face chosen by the player
     */
    private void placeCard(Face face){
        String value = chosenPositionButton.getText();
        idCard = value;
        String x = "", y = "";
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ',') {
                x = value.substring(0, i).trim();
                y = value.substring(i+1).trim();
            }
        }

        chosenPosition = new Position(Integer.parseInt(x), Integer.parseInt(y));

        chosenCard = null;

        try {
            client.place(myPlayer.getNickname(), face, chosenPosition);

        } catch (RequirementsNotMetException e) {
            showAlert("The requirements are not met");
            return;
        }catch (WrongTurnException e){
            showAlert(e.getMessage());
            return;
        }
        addFaceToBoard(face);

    }
    /**
     * This method places the StartingCard and the Player's token in the Board
     * This method sets the card and the token in the centre of the boardPane
     * @param face the face chosen by the player
     */
    private void placeStartingCard(Face face) {
        try {
            availableColors = client.placeStarting(myPlayer.getNickname(), face);
        } catch (WrongTurnException e){
            showAlert(e.getMessage());
            return;
        }

        handCard1.setEffect(null);
        handCard2.setEffect(null);
        handCard3.setEffect(null);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(face.getSrcImage())));
        startingCard.setImage(image);
        startingCard.setLayoutX(boardPane.getPrefWidth()/2 - startingCard.getFitWidth()/2 + 1);
        startingCard.setLayoutY(boardPane.getPrefHeight()/2 - startingCard.getFitHeight()/2);
        tokenOnStarting.setLayoutX(boardPane.getPrefWidth()/2 - tokenOnStarting.getFitWidth()/2);
        tokenOnStarting.setLayoutY(boardPane.getPrefHeight()/2 - tokenOnStarting.getFitHeight()/2 - startingCard.getFitHeight()/2);
        chosenCard = null;
    }
    /**
     * This method verifies if the user has selected all the values useful to move forward
     * @return boolean
     */
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

    List<Position> buttonsOutOfBounds = new ArrayList<>();

    /**
     * This method gives a graphical view of the possible position represented as buttons
     * This method stores in a list all the buttons whose dimensions
     * are out of the board's space
     */
    private void setupTurn() {
        Set<Position> availablePositions;
        try {
            availablePositions = client.getAvailablePositions(myPlayer.getNickname());
        } catch (WrongTurnException e){
            showAlert(e.getMessage());
            return;
        }

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

            while (x < 0) {
                offsetX -= constOffsetX;
                offsetY += constOffsetY;
                x++;
            }

            while (y > 0) {
                offsetY -= constOffsetY;
                offsetX -= constOffsetX;
                y--;
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

            if((offsetX < 0)||(offsetX + finalWidth > boardPane.getPrefWidth())||(offsetY < 0)||(offsetY + finalHeight > boardPane.getPrefHeight())) {
                buttonsOutOfBounds.add(position);
            }
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
    /**
     * This method reduces the size of the StartingCard and
     * sets the new cards' dimensions in order to keep the
     * right proportion inside the board's pane
     */
    private void resize (Pane pane, ImageView sCard, ImageView token, double offX, double offY, List<ImageView> cardsToBePlaced){
        double newHeight, newWidth;
        if(pane == boardPane) {
            newHeight = (pane.getChildren().getFirst().getLayoutBounds().getHeight())*0.80;
            newWidth = (pane.getChildren().getFirst().getLayoutBounds().getWidth())*0.80;
        } else {
            newHeight = sCard.getFitHeight()*0.8;
            newWidth = sCard.getFitWidth()*0.8;
        }


        offX = offX * 0.8;
        offY = offY * 0.8;

        sCard.setFitWidth(newWidth);
        sCard.setFitHeight(newHeight);
        sCard.setLayoutX(pane.getPrefWidth()/2-newWidth/2);
        sCard.setLayoutY((pane.getPrefHeight()/2-newHeight/2));
        token.setFitWidth(token.getFitWidth()*0.80);
        token.setFitHeight(token.getFitHeight()*0.80);

        token.setLayoutX(pane.getPrefWidth()/2 - token.getFitWidth()/2 + 1);
        token.setLayoutY(pane.getPrefHeight()/2 - token.getFitHeight()/2 - sCard.getFitHeight()/2);

        printImages(newWidth, newHeight, cardsToBePlaced, offX, offY, sCard);

    }
    List<ImageView> placedCards;
    String idCard;

    /**
     * This method modifies the dimensions and the layout of the Player's cards
     * using the new parametres setted by @see resize
     */
    private void printImages (double newWidth, double newHeight, List<ImageView> cardsToBePlaced, double offX, double offY, ImageView start){
        for(ImageView image : cardsToBePlaced){

            image.setFitWidth(newWidth);
            image.setFitHeight(newHeight);
            String value = image.getId();

            String x = "", y = "";

            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == ',') {
                    x = value.substring(0, i).trim();
                    y = value.substring(i+1).trim();
                }
            }

            int positionX = Integer.parseInt(x);
            int positionY = Integer.parseInt(y);

            Position position = new Position(positionX, positionY);
            Coordinates coords = switchFromPositionToCoords(position, offX, offY, start);

            double finalOffsetX = coords.getX();
            double finalOffsetY = coords.getY();

            image.setLayoutX(finalOffsetX);
            image.setLayoutY(finalOffsetY);
        }

    }
    /**
     * This method adds the chosen face in the board's pane
     * This method verifies if all the card are inside the board's pane
     * @param face chosen by player
     */
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
        card.setId(idCard);
        placedCards.add(card);
        boardPane.getChildren().add(card);

        if((!buttonsOutOfBounds.isEmpty())&&(buttonsOutOfBounds.contains(chosenPosition))){
            resize(boardPane, startingCard, tokenOnStarting, constOffsetX, constOffsetY, placedCards);
            constOffsetX = constOffsetX * 0.80;
            constOffsetY = constOffsetY * 0.80;
        }

        chosenPositionButton = null;
        Platform.runLater(() -> {
            List<Button> buttonsToRemove = new ArrayList<>(availablePositionsButtons);
            for (Button b : buttonsToRemove) {
                boardPane.getChildren().remove(b);
                b.setEffect(null);
            }
            availablePositionsButtons.clear();
        });

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

        for(Button b : playersBoardButtons){
            b.setDisable(true);
            b.setOpacity(0.5);
        }

        disableHandAndControlButtons();
        disablePickableButtons();

        List<PlayerClientModel> players = gameClientModel.getPlayers();
        for (PlayerClientModel p : players) {
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

                    for(Button b : playersBoardButtons){
                        b.setDisable(false);
                        b.setOpacity(1);
                    }

                    if(myPlayer.equals(gameClientModel.getCurrentPlayer()) &&
                            (gameClientModel.getCurrentState().equals(State.PLACE)))
                        enableHandAndControlButtons();
                    else if (myPlayer.equals(gameClientModel.getCurrentPlayer()) &&
                            gameClientModel.getCurrentState().equals(State.PICK))
                        enablePickableButtons();
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

    private void disablePickButton() {
        pickButton.setDisable(true);
        pickButton.setOpacity(0);
    }

    private void enablePickButton() {
        pickButton.setDisable(false);
        pickButton.setOpacity(1);
    }

    private void updateStateSetHand(){

        text = "";
        currentPlayer = "";

        if (gameClientModel.getCurrentPlayer().equals(myPlayer)) {
            text += "It's your turn! You have to place your starting card";
            enableHandAndControlButtons();
            disablePickableButtons();
            seeStandingsButton.setDisable(true);
            for(Button b : playersBoardButtons){
                b.setDisable(true);
                b.setOpacity(0.5);
            }
        } else {
            currentPlayer = gameClientModel.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to place the starting card";
            disableAll();
            seeStandingsButton.setDisable(true);
            for(Button b : playersBoardButtons){
                b.setDisable(true);
                b.setOpacity(0.5);
            }
        }
    }


    private void updateStateSetColor(){
        text = "";
        currentPlayer = "";

        if (gameClientModel.getCurrentPlayer().equals(myPlayer)) {
            text += "It's your turn! You have to choose your personal color";
            while (availableColors == null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            disableHandAndControlButtons();
            disablePickableButtons();
            seeStandingsButton.setDisable(true);
            for(Button b : playersBoardButtons){
                b.setDisable(true);
                b.setOpacity(0.5);
            }

            setColor();
        } else {
            currentPlayer = gameClientModel.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to choose the personal color";
            disableAll();
            seeStandingsButton.setDisable(true);
            for(Button b : playersBoardButtons){
                b.setDisable(true);
                b.setOpacity(0.5);

            }
        }
    }

    private void updateStateSetGoal() {
        text = "";
        currentPlayer = "";

        if (gameClientModel.getCurrentPlayer().equals(myPlayer)) {
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
            seeStandingsButton.setDisable(true);
            for(Button b : playersBoardButtons){
                b.setDisable(true);
                b.setOpacity(0.5);
            }

            setGoal();
        } else {
            currentPlayer = gameClientModel.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to choose the personal goal";
            disableAll();
            seeStandingsButton.setDisable(true);
            for(Button b : playersBoardButtons){
                b.setDisable(true);
                b.setOpacity(0.5);
            }
        }
    }

    private void updateStatePlace() {

        text = "";
        currentPlayer = "";

        if (gameClientModel.getCurrentPlayer().equals(myPlayer)) {

            text += "It's your turn! You have to place a card";
            boardPane.setOpacity(1);
            boardPane.setDisable(false);
            setupTurn();
            enableHandAndControlButtons();
        } else {
            currentPlayer = gameClientModel.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to place a card";
            disableAll();
        }
    }

    private void updateStatePick() {
        text = "";
        currentPlayer = "";

        if (gameClientModel.getCurrentPlayer().equals(myPlayer)) {
            text += "It's your turn! You have to pick a card";
            enablePickableButtons();
            disableHandAndControlButtons();
        } else {
            currentPlayer = gameClientModel.getCurrentPlayer().getNickname();
            text += currentPlayer + " is playing. " + currentPlayer + " has to pick a card";
            disableAll();
        }
    }

    private void updateStateLast() throws IOException {

        messagesThread.interrupt();

        String resource = "/it/polimi/ingsw/am42/javafx/Winning.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Parent root = fxmlLoader.load();
        WinningController winningController = fxmlLoader.getController();
        winningController.setClient(ClientHolder.getClient(), myPlayer);

        Platform.runLater(() -> {
            Scene scene = new Scene(root);
            Stage stage = HelloApplication.getStage();
            stage.setScene(scene);
            stage.show();
        });
    }

    private void updateStateDisconnected() {
        disableAll();
        showAlert("A player has disconnected from the game.");
        showAlert("The game will be closed.");
        System.exit(1);
    }

    private void updateHandMyPlayer() {
        List<PlayableCard> hand = myPlayer.getHand();
        for (ImageView imageView : this.hand) imageView.setImage(null);

        for (int i = 0; i < hand.size(); i++) {
            String src;
            if(hand.get(i) instanceof StartingCard)
                src = hand.get(i).getBack().getSrcImage();
            else
                src = hand.get(i).getFront().getSrcImage();
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(src)));
            this.hand.get(i).setImage(image);
        }
    }

    private void updatePickableCards() {
        List<PlayableCard> pickableResources = gameClientModel.getPickableResourceCards();
        List<PlayableCard> pickableGold = gameClientModel.getPickableGoldCards();

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

    private void updateColorBoardPlayers() {
        List<PlayerClientModel> tmp = new ArrayList<>();
        for(PlayerClientModel player : gameClientModel.getPlayers()) {
            if(player.getColor() != null && !player.equals(myPlayer)) {
                tmp.add(player);
                PlayersColor color = player.getColor();
                Button button = playersBoardButtons.get(tmp.indexOf(player));
                switch (color) {
                    case RED -> button.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
                    case GREEN -> button.setStyle("-fx-background-color: green; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
                    case YELLOW -> button.setStyle("-fx-background-color: yellow; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10;");
                    case BLUE -> button.setStyle("-fx-background-color: blue; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
                }
            }
        }
    }

    private Coordinates switchFromPositionToCoords(Position position, double offX, double offY, ImageView sCard) {
        int x = position.getX();
        int y = position.getY();

        double tmpx = sCard.getLayoutX();
        double tmpy = sCard.getLayoutY();

        while (x > 0) {
            tmpx += offX;
            tmpy -= offY;
            x--;
        }

        while (x < 0) {
            tmpx -= offX;
            tmpy += offY;
            x++;
        }

        while (y > 0) {
            tmpy -= offY;
            tmpx -= offX;
            y--;
        }

        while (y < 0) {
            tmpy += offY;
            tmpx += offX;
            y++;
        }

        return new Coordinates(tmpx, tmpy);

    }

    private Image getTokenColor(PlayersColor color) {
        switch (color) {
            case RED -> {
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/red.png")));
            }
            case BLUE -> {
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/blue.png")));
            }

            case YELLOW -> {
               return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/yellow.png")));
            }

            case GREEN -> {
                return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/tokens/green.png")));
            }
        }
        return null;
    }

Pane board;
    private int loadBoard (PlayerClientModel player, Pane tmpBoard){

        board = tmpBoard;
        List<ImageView> cardToBePlaced = new ArrayList<>();
        ImageView sCard = null;
        double offXOtherPlayer = 80.5;
        double offYOtherPlayer = 40.5;

        ImageView token;
        if(player.getColor() != null)
            token = new ImageView(getTokenColor(player.getColor()));
        else {
            token = null;
        }

        for(Face face : player.getBoard().getFaces()){

            if(player.getBoard().getFaces().getFirst().equals(face)) {

                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(face.getSrcImage())));

                if(player == myPlayer) {
                    startingCard.setImage(image);
                    sCard = startingCard;
                }
                else{
                    sCard = new ImageView(image);
                }
                ImageView finalSCard = sCard;

                Platform.runLater(() -> {
                    if(!(finalSCard==startingCard)) {
                        board.getChildren().add(finalSCard);
                        board.getChildren().add(token);
                    }
                });

                finalSCard.setFitWidth(110);
                finalSCard.setFitHeight(70);
                finalSCard.setLayoutX(board.getPrefWidth()/2 - finalSCard.getFitWidth()/2 + 1);
                finalSCard.setLayoutY(board.getPrefHeight()/2 - finalSCard.getFitHeight()/2);

                token.setFitWidth(32);
                token.setFitHeight(32);
                token.setLayoutX(board.getPrefWidth()/2 - token.getFitWidth()/2 + 1);
                token.setLayoutY(board.getPrefHeight()/2 - token.getFitHeight()/2 - sCard.getFitHeight()/2);

            }
            else{
                Coordinates coords = switchFromPositionToCoords(face.getPosition(), offXOtherPlayer, offYOtherPlayer, sCard);
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(face.getSrcImage())));
                ImageView card = new ImageView(image);

                if(player == myPlayer){
                    placedCards.add(card);
                }
                Platform.runLater(() -> {
                    board.getChildren().add(card);
                });

                card.setLayoutX(coords.getX());
                card.setLayoutY(coords.getY());
                card.setFitWidth(110);
                card.setFitHeight(70);
                String id = face.getPosition().getX() + ", " + face.getPosition().getY();
                card.setId(id);
                cardToBePlaced.add(card);

            }

        }

        boolean finished = false;
        int counterNumberResizeDone = 0;

        while(!finished) {
            finished = true;
            for(ImageView image : cardToBePlaced){
                double x = image.getLayoutX();
                double y = image.getLayoutY();

                if((x < 0)||(x + image.getFitWidth() > board.getPrefWidth())||(y < 0)||(y + image.getFitHeight() > board.getPrefHeight())) {
                    resize(board, sCard, token, offXOtherPlayer, offYOtherPlayer, cardToBePlaced);
                    counterNumberResizeDone++;
                    offXOtherPlayer = offXOtherPlayer * 0.8;
                    offYOtherPlayer = offYOtherPlayer * 0.8;
                    finished = false;
                    break;
                }
            }
        }
        return counterNumberResizeDone;
    }
    public void showOtherBoardPlayer(String name, int index){
        for(Button b : playersBoardButtons){
            b.setOpacity(0.5);
            b.setDisable(true);
        }

        seeStandingsButton.setDisable(true);

        disableHandAndControlButtons();
        disablePickableButtons();

        PlayerClientModel player = gameClientModel.getPlayer(name);

        boardPane.setOpacity(0);
        loadBoard(player, boardPaneOtherPlayer);

        boardLabelOtherPlayer.setOpacity(1);
        boardLabelOtherPlayer.setVisible(true);
        boardLabelOtherPlayer.setText(player.getNickname() + "'s board");
        boardLabelOtherPlayer.setStyle(playersBoardButtons.get(index).getStyle());


        boardPaneOtherPlayer.setOpacity(1);
        boardPaneOtherPlayer.setDisable(false);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> {
                    for(Button b : playersBoardButtons){
                        b.setOpacity(1);
                        b.setDisable(false);
                    }
                    if(myPlayer.equals(gameClientModel.getCurrentPlayer()) &&
                            gameClientModel.getCurrentState().equals(State.PLACE)) enableHandAndControlButtons();
                    else if(myPlayer.equals(gameClientModel.getCurrentPlayer()) &&
                            gameClientModel.getCurrentState().equals(State.PICK)) enablePickableButtons();

                    boardPaneOtherPlayer.setOpacity(0);
                    boardPaneOtherPlayer.setDisable(true);
                    boardPane.setOpacity(1);
                    boardPaneOtherPlayer.getChildren().clear();
                    boardLabelOtherPlayer.setOpacity(0);
                    boardLabelOtherPlayer.setVisible(false);

                    seeStandingsButton.setDisable(false);

                }
        ));
        timeline.setCycleCount(1);
        timeline.play();
    }


    public void player1BoardButtonAction(ActionEvent event) {
        String text = player1Board.getText();
        text = text.substring(0, text.length() - 8);
        showOtherBoardPlayer(text, 0);
    }

    public void player2BoardButtonAction(ActionEvent event) {
        String text = player2Board.getText();
        text = text.substring(0, text.length() - 8);
        showOtherBoardPlayer(text, 1);
    }

    public void player3BoardButtonAction(ActionEvent event) {
        String text = player3Board.getText();
        text = text.substring(0, text.length() - 8);
        showOtherBoardPlayer(text, 2);
    }

    private void loadAllBoard() {
        int counter = loadBoard(myPlayer, boardPane);
        for(int i=0; i<counter; i++){
            constOffsetX = constOffsetX * 0.8;
            constOffsetY = constOffsetY * 0.8;
        }
        System.out.println("Loading my boards");
    }


    public void updateGameView() {
        while (true) {
            if (gameClientModel.getNewUpdate()) {

                if(gameToBeLoad) {
                    loadAllBoard();
                    setBackgroundNickname(myPlayer.getColor());
                    updateColorBoardPlayers();
                    gameToBeLoad = false;
                    if (myPlayer.getPersonalGoal() != null) {
                        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(myPlayer.getPersonalGoal().getSrcImage())));
                        personalGoal.setImage(image);
                    }
                    seeStandingsButton.setDisable(false);
                    for(Button b : playersBoardButtons){
                        b.setDisable(false);
                        b.setOpacity(1);
                    }
                }

                if (gameClientModel.getCurrentState().equals(State.SETHAND)) {
                   updateStateSetHand();
                } else if (gameClientModel.getCurrentState().equals(State.SETCOLOR)) {
                    updateStateSetColor();
                } else if (gameClientModel.getCurrentState().equals(State.SETGOAL)) {
                    updateColorBoardPlayers();
                    updateStateSetGoal();
                } else if (gameClientModel.getCurrentState().equals(State.PLACE)) {
                    seeStandingsButton.setDisable(false);
                    for(Button b : playersBoardButtons){
                        b.setDisable(false);
                        b.setOpacity(1);
                    }
                    if(gameClientModel.isTurnFinal() && gameClientModel.getCurrentPlayer().equals(gameClientModel.getPlayers().getFirst()))
                        Platform.runLater(() -> showAlert("This is the final turn. You will only be able to place a card."));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    updateStatePlace();
                } else if (gameClientModel.getCurrentState().equals(State.PICK)) {
                    updateStatePick();
                } else if (gameClientModel.getCurrentState().equals(State.LAST)) {
                    try {
                        updateStateLast();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    updateStateDisconnected();
                }

                updateHandMyPlayer();

                updatePickableCards();



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
