package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gameview.GameView;
import it.polimi.ingsw.am42.view.gameview.PlayerView;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Pair;

import java.util.List;

public class BoardController {

    private Client client;

    @FXML
    protected ImageView circle;
    private GameView gameView;
    private String modifiedPlayer;

    public BoardController() {}

    public void displayStandings(){
        gameView = client.getView();
        modifiedPlayer = gameView.getModifiedPlayer();
        for(PlayerView p : gameView.getPlayers()) {
            if(p.getNickname().equals(modifiedPlayer)) {
                int points = p.getPoints();
                String color = p.getColor().toString();


            }
        }


        /*
        vedo il DIFF, capisco chi ha fatto punti, setCircleX o setCircleY,
         */

    }


    public void setClient(Client client) {
        this.client = client;
    }

}

