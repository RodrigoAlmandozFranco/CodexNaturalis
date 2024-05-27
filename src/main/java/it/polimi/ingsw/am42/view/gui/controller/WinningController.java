package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.view.clientModel.GameClientModel;
import it.polimi.ingsw.am42.view.clientModel.PlayerClientModel;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WinningController {
    private Client client;
    private GameClientModel gameClientModel;
    private List<Player> winners;
    private PlayerClientModel myPlayer;
    @FXML
    ListView listView;
    @FXML
    ChoiceBox<String> choiceBox;
    @FXML
    TextField textField;
    @FXML
    Label thirdPlayerLabel, secondPlayerLabel, firstPlayerLabel, label;

    public WinningController() {}

    public void setClient(Client client, PlayerClientModel myPlayer) {
        this.client = client;
        this.myPlayer = myPlayer;
        gameClientModel = client.getView();
        initializeMessages();
        setLabel();
        showPodium();
    }

    private void setLabel() {
        label.setStyle("-fx-background-color: brown; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        label.setWrapText(true);
        String text = "";
        text += "You achieved + " + myPlayer.getPoints() + " points and " + myPlayer.getNumberGoalsAchieved() + " goals! Well done!\n";
        for(PlayerClientModel playerClientModel : gameClientModel.getPlayers()) {
            if(!playerClientModel.getNickname().equals(myPlayer.getNickname())) {
                text += playerClientModel.getNickname() + " achieved + " + playerClientModel.getPoints() + " points and "
                        + playerClientModel.getNumberGoalsAchieved() + " goals!\n";
            }
        }
        label.setText(text);
    }

    private void initializeMessages() {
        List<String> nicknames = gameClientModel.getPlayers().stream().map(PlayerClientModel::getNickname).collect(Collectors.toList());
        nicknames.addFirst("All");
        nicknames.remove(myPlayer.getNickname());
        choiceBox.getItems().addAll(nicknames);
        choiceBox.setValue("All");
        Thread thread = new Thread(this::seeMessages);
        thread.start();
    }

    public void showPodium() {

        firstPlayerLabel.setStyle("-fx-background-color: gold; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10;");
        secondPlayerLabel.setStyle("-fx-background-color: silver; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10;");
        thirdPlayerLabel.setStyle("-fx-background-color: #cd7f32; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 10;");


        List<PlayerClientModel> standings = gameClientModel.getPlayers();

        standings = standings.stream().sorted(Comparator.comparingInt(PlayerClientModel::getPoints).reversed()).toList();

        List<Player> winners = client.getWinner();

        List<String> winnersNicknames = winners.stream().map(Player::getNickname).toList();

        firstPlayerLabel.setWrapText(true);

        int numberWinners = winners.size();

        if(numberWinners > 1) {
            for (Player winner : winners) {
                firstPlayerLabel.setText(winner.getNickname() + "\n");
            }
        } else {
            firstPlayerLabel.setText(winners.getFirst().getNickname());
        }

        for(PlayerClientModel playerClientModel : standings) {
            if(!winnersNicknames.contains(playerClientModel.getNickname())) {
                if(secondPlayerLabel.getText().isEmpty()) {
                    secondPlayerLabel.setText(playerClientModel.getNickname());
                } else if(thirdPlayerLabel.getText().isEmpty()){
                    thirdPlayerLabel.setText(playerClientModel.getNickname());
                }
            }
        }


        firstPlayerLabel.setTranslateX(-100);
        secondPlayerLabel.setTranslateX(0);
        thirdPlayerLabel.setTranslateX(100);

        firstPlayerLabel.setOpacity(0);
        firstPlayerLabel.setScaleX(0.5);
        firstPlayerLabel.setScaleY(0.5);
        secondPlayerLabel.setOpacity(0);
        secondPlayerLabel.setScaleX(0.5);
        secondPlayerLabel.setScaleY(0.5);
        thirdPlayerLabel.setOpacity(0);
        thirdPlayerLabel.setScaleX(0.5);
        thirdPlayerLabel.setScaleY(0.5);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(thirdPlayerLabel.translateXProperty(), 100),
                        new KeyValue(thirdPlayerLabel.opacityProperty(), 0),
                        new KeyValue(thirdPlayerLabel.scaleXProperty(), 0.5),
                        new KeyValue(thirdPlayerLabel.scaleYProperty(), 0.5)),
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(thirdPlayerLabel.translateXProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(thirdPlayerLabel.opacityProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(thirdPlayerLabel.scaleXProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(thirdPlayerLabel.scaleYProperty(), 1, Interpolator.EASE_BOTH)),

                new KeyFrame(Duration.seconds(0),
                        new KeyValue(secondPlayerLabel.translateXProperty(), 0),
                        new KeyValue(secondPlayerLabel.opacityProperty(), 0),
                        new KeyValue(secondPlayerLabel.scaleXProperty(), 0.5),
                        new KeyValue(secondPlayerLabel.scaleYProperty(), 0.5)),
                new KeyFrame(Duration.seconds(5),
                        new KeyValue(secondPlayerLabel.translateXProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(secondPlayerLabel.opacityProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(secondPlayerLabel.scaleXProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(secondPlayerLabel.scaleYProperty(), 1, Interpolator.EASE_BOTH)),

                new KeyFrame(Duration.seconds(0),
                        new KeyValue(firstPlayerLabel.translateXProperty(), -100),
                        new KeyValue(firstPlayerLabel.opacityProperty(), 0),
                        new KeyValue(firstPlayerLabel.scaleXProperty(), 0.5),
                        new KeyValue(firstPlayerLabel.scaleYProperty(), 0.5)),
                new KeyFrame(Duration.seconds(7),
                        new KeyValue(firstPlayerLabel.translateXProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(firstPlayerLabel.opacityProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(firstPlayerLabel.scaleXProperty(), 1, Interpolator.EASE_BOTH),
                        new KeyValue(firstPlayerLabel.scaleYProperty(), 1, Interpolator.EASE_BOTH))
        );
        timeline.play();
    }


    public void seeMessages() {
        updateListView(gameClientModel.getAllMessages());

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
                    e.printStackTrace();
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






}