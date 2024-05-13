package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.network.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController extends Controller{
    @FXML
    private Button playGameButton;

    @FXML
    private Button loadGameButton;

    public MenuController(){
    }

    public void playGameAction() {
        System.out.println("playGameAction");
    }

    public void loadGameAction() {
        System.out.println("loadGameAction");
    }
}
