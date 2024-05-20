package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuController {
    @FXML
    private Button createGameButton;

    @FXML
    private Button loadGameButton;

    private Client client;

    public MenuController(){
    }


    public void setClient(Client client){
        this.client = client;
        createGameButton.setOnMouseEntered(event -> createGameButton.setCursor(Cursor.HAND));
        createGameButton.setOnMouseExited(event -> createGameButton.setCursor(Cursor.DEFAULT));
        loadGameButton.setOnMouseEntered(event -> loadGameButton.setCursor(Cursor.HAND));
        loadGameButton.setOnMouseExited(event -> loadGameButton.setCursor(Cursor.DEFAULT));
    }

    public void createGameAction(ActionEvent event) throws IOException {
        String resource = "/it/polimi/ingsw/am42/javafx/NicknameFirstPlayerCreateGame.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Parent root = fxmlLoader.load();
        FirstPlayerCreateGameController first = fxmlLoader.getController();
        first.setClient(ClientHolder.getClient());

        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void loadGameAction(ActionEvent event) throws IOException {
        String resource = "src/main/resources/it/polimi/ingsw/am42/nicknameLoadGame.fxml";
        FXMLLoader root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(resource)));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root.load());
        //TODO there isn't the constructor
        stage.setScene(scene);
        stage.show();
    }
}