package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gameview.GameView;

public class Controller {
    protected Client client;
    protected GameView gameView;

    public Controller (Client client){
        this.client = client;
        gameView = client.getView();
    }

    public Controller(){}
}
