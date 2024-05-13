package it.polimi.ingsw.am42.view.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuController {
    @FXML
    private Button playGameButton;

    @FXML
    private Button loadGameButton;

    public void playGameAction() {
        System.out.println("playGameAction");
    }

    public void loadGameAction() {
        System.out.println("loadGameAction");
    }
}
