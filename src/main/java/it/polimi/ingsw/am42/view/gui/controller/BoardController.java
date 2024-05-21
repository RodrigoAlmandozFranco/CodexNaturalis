package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.cards.types.playables.ResourceCard;
import it.polimi.ingsw.am42.model.cards.types.playables.StartingCard;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.view.gameview.GameView;
import it.polimi.ingsw.am42.view.gameview.PlayerView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

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
    //hand cards end

    Face chosenFace;
    Position chosenPosition;

    //goal cards
    List<ImageView> globalGoals;

    @FXML
    ImageView personalGoal, globalGoal1, globalGoal2;
    //end goal cards

    @FXML
    Button placeFrontButton, seeFrontButton, placeBackButton, seeBackButton;

    @FXML
    Label updateText;

    public BoardController() {}

    public void placeFrontButtonAction(ActionEvent event) {
        if (checkBeforePlace()) return;

        if(chosenCard instanceof StartingCard) {
            placeStartingCard(chosenCard.getBack());
            return;
        }

        chosenFace = chosenCard.getFront();
        try {
            client.place(myPlayer.getNickname(), chosenFace, chosenPosition);
        } catch (RequirementsNotMetException e) {
            showAlert("The requirements are not met");
        }
    }

    private void placeStartingCard(Face face) {
        client.placeStarting(myPlayer.getNickname(), face);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void seeFrontButtonAction(ActionEvent event) {
        if(chosenCard == null) {
            showAlert("You have to choose a card");
            return;
        }

       if(chosenCard instanceof StartingCard) {
            seeBackStartingCard();
       } else {
            modifyHandCardImage(chosenCard.getFront().getSrcImage());
       }
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

    public void placeBackButtonAction(ActionEvent event) {
        if (checkBeforePlace()) return;

        if(chosenCard instanceof StartingCard) {
            placeStartingCard(chosenCard.getFront());
            return;
        }

        chosenFace = chosenCard.getBack();
        try {
            client.place(myPlayer.getNickname(), chosenFace, chosenPosition);
        } catch (RequirementsNotMetException e) {
            showAlert("The requirements are not met");
        }
    }

    private boolean checkBeforePlace() {
        if (chosenCard == null) {
            showAlert("You have to choose a card");
            return true;
        }
        if(chosenPosition == null && !(chosenCard instanceof StartingCard)) {
            showAlert("You have to choose a position");
            return true;
        }
        return false;
    }

    public void seeBackButtonAction(ActionEvent event) {
        if(chosenCard == null) {
            showAlert("You have to choose a card");
            return;
        }
        if(chosenCard instanceof StartingCard) {
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

    private void highlightCard(ImageView cardImageView) {
        for (ImageView handCard : hand) {
            handCard.setEffect(null);
        }
        cardImageView.setEffect(highlightEffect);
    }


    private void enablePickableButtons() {
        for(Button b : pickableCardsButton) {
            b.setDisable(false);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.HAND));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
    }

    private void disablePickableButtons() {
        for(Button b : pickableCardsButton) {
            b.setDisable(true);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.DEFAULT));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
    }

    private void enableHandAndControlButtons() {
        for(Button b : handAndControlButtons) {
            b.setDisable(false);
            if(b.equals(placeFrontButton) || b.equals(placeBackButton) || b.equals(seeFrontButton) || b.equals(seeBackButton))
                b.setOpacity(1);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.HAND));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
    }

    private void disableHandAndControlButtons() {
        for(Button b : handAndControlButtons) {
            b.setDisable(true);
            if(b.equals(placeFrontButton) || b.equals(placeBackButton) || b.equals(seeFrontButton) || b.equals(seeBackButton))
                b.setOpacity(0);
            b.setOnMouseEntered(event -> b.setCursor(Cursor.DEFAULT));
            b.setOnMouseExited(event -> b.setCursor(Cursor.DEFAULT));
        }
    }



    public void setClient(Client client) {
        this.client = client;
        gameView = client.getView();
        Thread thread = new Thread(this::seeMessages);
        thread.start();
        Thread threadGame = new Thread(this::updateGameView);
        threadGame.start();
        this.start();
    }

    public void updateGameView() {
        while(true){
            if(gameView.getNewUpdate()){

                if(gameView.getCurrentPlayer().getNickname().equals(gameView.getNickname())) {
                    if(gameView.getCurrentState().toString().equals("SETHAND")) {
                        enableHandAndControlButtons();
                        disablePickableButtons();
                    } else if(gameView.getCurrentState().toString().equals("SETCOLOR")) {
                        //todo
                    } else if(gameView.getCurrentState().toString().equals("SETGOAL")) {
                        //todo
                    } else if(gameView.getCurrentState().toString().equals("PLACE")) {
                        if(gameView.getCurrentPlayer().equals(myPlayer)) {
                            enableHandAndControlButtons();
                        } else {
                            disableHandAndControlButtons();
                            disablePickableButtons();
                        }
                    } else if(gameView.getCurrentState().toString().equals("PICK")) {
                        if(gameView.getCurrentPlayer().equals(myPlayer)) {
                            enablePickableButtons();
                        } else {
                            disableHandAndControlButtons();
                            disablePickableButtons();
                        }
                    } else if(gameView.getCurrentState().toString().equals("LAST")) {
                        //todo
                    } else {
                        // case disconnected
                        //todo
                    }
                } else {
                    disableHandAndControlButtons();
                    disablePickableButtons();
                }

                List<PlayableCard> hand = myPlayer.getHand();
                for(int i = 0; i < hand.size(); i++) {
                    String src = hand.get(i).getBack().getSrcImage();
                    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(src)));
                    this.hand.get(i).setImage(image);
                }

                List<PlayableCard> pickableResources = gameView.getPickableResourceCards();
                List<PlayableCard> pickableGold = gameView.getPickableGoldCards();

                List<List<PlayableCard>> pickables = new ArrayList<>();
                pickables.add(pickableResources);
                pickables.add(pickableGold);

                for(List<PlayableCard> pickable : pickables) {
                    ImageView firstCard;
                    List<ImageView> pickableCards;
                    if(pickable.getFirst() instanceof ResourceCard) {
                        firstCard = firstCardResource;
                        pickableCards = pickableResourceCards;
                    } else {
                        firstCard = firstCardGold;
                        pickableCards = pickableGoldCards;
                    }
                    for(PlayableCard p : pickable) {
                        if(!p.getVisibility()) {
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


                //todo points + boards




                String currentPlayer;
                String text = "";

                if(gameView.getCurrentPlayer().equals(myPlayer)) {
                    text += "It's your turn";
                    currentPlayer = "You have ";
                } else {
                    currentPlayer = gameView.getCurrentPlayer().getNickname();
                    text += currentPlayer + " is playing";
                    currentPlayer += " has ";
                }



                if(gameView.getCurrentState().toString().equals("SETHAND")) {
                    //todo
                } else if(gameView.getCurrentState().toString().equals("SETCOLOR")) {
                    text += currentPlayer + "to choose a color.";
                } else if(gameView.getCurrentState().toString().equals("SETGOAL")) {
                    text += currentPlayer + "to choose your personal goal.";
                } else if(gameView.getCurrentState().toString().equals("PLACE")) {
                    text += currentPlayer + "to place a card.";
                    if(currentPlayer.equals("You have ")) {
                        text += "Pick a position, choose a card and select the face you want to place.";
                    }
                } else if(gameView.getCurrentState().toString().equals("PICK")) {
                    text += currentPlayer + "to pick a card.";
                    if(currentPlayer.equals("You have ")) {
                        text += "Pick a card from the top left of the screen. You can pick the visible cards or one card from the decks";
                    }
                } else if(gameView.getCurrentState().toString().equals("LAST")) {
                    //todo
                } else {
                    // case disconnected
                    //todo
                }

                String finalText = text;
                Platform.runLater(() -> {
                    updateText.setStyle("-fx-background-color: #FFEBCD; -fx-text-fill: #DC143C; -fx-padding: 10;");
                    updateText.setWrapText(true);
                    updateText.setTextAlignment(TextAlignment.CENTER);
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





    public void seeMessages(){

        while(true){
            List<ChatMessage> newMessage = gameView.getTmpMessages();
            if(newMessage != null && !newMessage.isEmpty()) {
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
            if(gameView.getNickname().equals(message.getSender()))
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

        if(message.isEmpty()) return;

        textField.clear();

        choiceBox.setValue("All");

        ChatMessage chatMessage;

        if(receiver.equals("All"))
            chatMessage = new ChatMessage(message, sender);
        else
            chatMessage = new ChatMessage(message, sender, receiver);
        client.sendChatMessage(chatMessage);
    }


    public void start(){

        players = gameView.getPlayers();
        nicknames = new ArrayList<>();
        nicknames.add("All");

        for(PlayerView p : players){
            if (!p.getNickname().equals(gameView.getNickname()))
                nicknames.add(p.getNickname());
            else
                myPlayer = p;
        }

        Platform.runLater(() -> {
            choiceBox.getItems().clear();
            choiceBox.getItems().addAll(nicknames);
            choiceBox.setValue("All");
        });

        List<GoalCard> goals = gameView.getGlobalGoals();
        for(int i = 0; i < goals.size(); i++) {
            String src = goals.get(i).getSrcImage();
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(src)));
            globalGoals.get(i).setImage(image);
        }

    }


    public void handCard1ButtonAction(ActionEvent event) {
        chosenCard = myPlayer.getHand().getFirst();
        highlightCard(handCard1);
    }

    public void handCard2ButtonAction(ActionEvent event) {
        chosenCard = myPlayer.getHand().get(1);
        highlightCard(handCard2);
    }

    public void handCard3ButtonAction(ActionEvent event) {
        chosenCard = myPlayer.getHand().get(2);
        highlightCard(handCard3);
    }

    public void firstCardResourceButtonEvent(ActionEvent event) {
        PlayableCard card = null;
        for(PlayableCard p : gameView.getPickableResourceCards()) {
            if(!p.getVisibility()) {
                card = p;
                break;
            }
        }
        client.pick(myPlayer.getNickname(), card);
    }

    public void firstCardGoldButtonEvent(ActionEvent event) {
        PlayableCard card = null;
        for(PlayableCard p : gameView.getPickableGoldCards()) {
            if(!p.getVisibility()) {
                card = p;
                break;
            }
        }
        client.pick(myPlayer.getNickname(), card);
    }

    public void pickableResource1ButtonEvent(ActionEvent event) {
        client.pick(myPlayer.getNickname(), gameView.getPickableResourceCards().getFirst());
    }

    public void pickableGold1ButtonEvent(ActionEvent event) {
        client.pick(myPlayer.getNickname(), gameView.getPickableGoldCards().getFirst());
    }

    public void pickableResource2ButtonEvent(ActionEvent event) {
        client.pick(myPlayer.getNickname(), gameView.getPickableResourceCards().get(1));
    }

    public void pickableGold2ButtonEvent(ActionEvent event) {
        client.pick(myPlayer.getNickname(), gameView.getPickableGoldCards().get(1));
    }



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

    }
}

