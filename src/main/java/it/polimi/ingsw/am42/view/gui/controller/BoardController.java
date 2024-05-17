package it.polimi.ingsw.am42.view.gui.controller;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardController implements Initializable {

    private Client client;
    private GameView gameView;
    private String modifiedPlayer;

    private List<PlayerView> players;

    private List<String> nicknames;



    //@FXML
    //VBox vbox;
    @FXML
    ListView<Label> listView;
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

    public BoardController() {

    }

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
            if(client.getView().getCanRead()){
                List<ChatMessage> newMessage = client.getView().getTmpMessages();
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
            if(client.getView().getMyNickname().equals(message.getSender()))
                sender = "You";
            else
                sender = message.getMessage();

            Label messageLabel = new Label(sender + " to " + message.getReceiver() + ": " + message.getMessage());

            messageLabel.setStyle("-fx-font-weight: bold;");
            listView.getItems().add(messageLabel);
        }
    }

    public void sendMessage(ActionEvent e) {
        String message = textField.getText();
        String sender = client.getView().getMyNickname();
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
        System.out.println("Messaggio inviato");
    }


    public void start(){

        players = client.getView().getPlayers();
        nicknames = new ArrayList<>();
        nicknames.add("All");

        for(PlayerView p : players){
            if (!p.getNickname().equals(client.getView().getMyNickname()))
                nicknames.add(p.getNickname());
        }

        Platform.runLater(() -> {
            choiceBox.getItems().clear();
            choiceBox.getItems().addAll(nicknames);
            choiceBox.setValue("All");
        });
        //vbox.getChildren().add(listView);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Thread thread = new Thread(this::seeMessages);
        thread.start();

//        ObservableList<Label> messages = FXCollections.observableArrayList();
//        listView.setItems(messages);

        choiceBox.setValue("All");

        //scrollPane.setContent(vbox);

    }
}

