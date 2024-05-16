package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gameview.GameView;
import it.polimi.ingsw.am42.view.gameview.PlayerView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WinningController {
    private Client client;
    private List<Player> winners;

    @FXML
    Label thirdPlayerLabel, secondPlayerLabel, firstPlayerLabel;

    public WinningController() {}

    public void setClient(Client client) {
        this.client = client;
    }

    public void showPodium() {
        GameView gameView = client.getView();
        List<PlayerView> standings = gameView.getPlayers();

        standings = standings.stream().sorted(Comparator.comparingInt(PlayerView::getPoints).reversed())
                                        .collect(Collectors.toList());

        List<Player> winners = client.getWinner();

        List<String> winnersNicknames = winners.stream().map(Player::getNickname).collect(Collectors.toList());

        firstPlayerLabel.setWrapText(true);

        int numberWinners = winners.size();

        if(numberWinners > 1) {
            for (Player winner : winners) {
                firstPlayerLabel.setText(winner.getNickname() + "\n");
            }
        } else {
            firstPlayerLabel.setText(winners.getFirst().getNickname());
        }

        for(PlayerView playerView : standings) {
            if(!winnersNicknames.contains(playerView.getNickname())) {
                if(secondPlayerLabel.getText().isEmpty()) {
                    secondPlayerLabel.setText(playerView.getNickname());
                } else {
                    thirdPlayerLabel.setText(playerView.getNickname());
                }
            }
        }


        firstPlayerLabel.setTranslateX(-100);
        secondPlayerLabel.setTranslateX(0);
        thirdPlayerLabel.setTranslateX(100);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(1), new KeyValue(thirdPlayerLabel.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(secondPlayerLabel.translateXProperty(), 0)),
                new KeyFrame(Duration.seconds(3), new KeyValue(firstPlayerLabel.translateXProperty(), 0))
        );
        timeline.play();

    }
}
