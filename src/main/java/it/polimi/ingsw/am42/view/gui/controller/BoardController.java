package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    private List<PlayerView> players;
    private List<String> nicknames;
    List<Button> pickableCardsButton;
    List<ImageView> pickableResourceCards;
    List<ImageView> pickableGoldCards;
    List<ImageView> globalGoals;
    List<ImageView> hand;
    List<Button> handButtons;

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
    @FXML
    ImageView firstCardResource, firstCardGold, pickableResource1, pickableResource2, pickableGold1, pickableGold2;
    @FXML
    Button firstCardResourceButton, pickableResource1Button, pickableResource2Button;
    @FXML
    Button firstCardGoldButton, pickableGold1Button, pickableGold2Button;
    //Pickable cards labels end

    //hand cards
    @FXML
    ImageView handCard1, handCard2, handCard3;
    @FXML
    Button handCard1Button, handCard2Button, handCard3Button;
    //hand cards end

    //goal cards
    @FXML
    ImageView personalGoal, globalGoal1, globalGoal2;
    //end goal cards


    @FXML
    Label updateText;

    public BoardController() {}

//    public void displayStandings(){
//        gameView = client.getView();
//        modifiedPlayer = gameView.getModifiedPlayer();
//        for(PlayerView p : gameView.getPlayers()) {
//            if(p.getNickname().equals(modifiedPlayer)) {
//                int points = p.getPoints();
//                String color = p.getColor().toString();
//
//
//            }
//        }


        /*
        vedo il DIFF, capisco chi ha fatto punti, setCircleX o setCircleY,
         */

//    }


    public void setClient(Client client) {
        this.client = client;
        gameView = client.getView();
        this.start();
    }

    public void seeMessages(){
        while(client == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(true){
            if(gameView.getCanRead()){
                List<ChatMessage> newMessage = gameView.getTmpMessages();
                if (newMessage != null && !newMessage.isEmpty()) {
                    Platform.runLater(() -> {
                        updateListView(newMessage);
                    });
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateListView(List<ChatMessage> newMessage) {
        for (ChatMessage message : newMessage) {
            String sender;
            if(gameView.getMyNickname().equals(message.getSender()))
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
        String sender = gameView.getMyNickname();
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
            if (!p.getNickname().equals(gameView.getMyNickname()))
                nicknames.add(p.getNickname());
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
        //todo
    }

    public void handCard2ButtonAction(ActionEvent event) {
        //todo
    }

    public void handCard3ButtonAction(ActionEvent event) {
        //todo
    }

    public void firstCardResourceButtonEvent(ActionEvent event) {
        //todo

    }

    public void firstCardGoldButtonEvent(ActionEvent event) {
        //todo
    }

    public void pickableResource1ButtonEvent(ActionEvent event) {
        //todo
    }

    public void pickableGold1ButtonEvent(ActionEvent event) {
        //todo
    }

    public void pickableResource2ButtonEvent(ActionEvent event) {
        //todo
    }

    public void pickableGold2ButtonEvent(ActionEvent event) {
        //todo
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread thread = new Thread(this::seeMessages);
        thread.start();

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

        handButtons = new ArrayList<>();
        handButtons.add(handCard1Button);
        handButtons.add(handCard2Button);
        handButtons.add(handCard3Button);
    }
}

