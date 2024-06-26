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
/**
 * This class represents the GUI controller that controls the phase
 * during which the first Player has to choose between two options: create a new game or
 * load an existing game
 *
 * @author Mattia Brandi
 * @author Rodrigo Almandoz Franco
 */
public class MenuController {
    @FXML
    private Button createGameButton;

    @FXML
    private Button loadGameButton;

    private Client client;

    public MenuController(){
    }

    /**
     * This method sets the Client in the Controller, and it sets the cursor's shape
     * based on its position on the screen
     *
     * @param client client instance
     */
    public void setClient(Client client){
        this.client = client;
        createGameButton.setOnMouseEntered(event -> createGameButton.setCursor(Cursor.HAND));
        createGameButton.setOnMouseExited(event -> createGameButton.setCursor(Cursor.DEFAULT));
        loadGameButton.setOnMouseEntered(event -> loadGameButton.setCursor(Cursor.HAND));
        loadGameButton.setOnMouseExited(event -> loadGameButton.setCursor(Cursor.DEFAULT));
    }

    /**
     * This method is called once the associated button is clicked.
     * The player clicks this button if he wants to create a new Game.
     * This method loads the right scene and sets the new controller
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during loading the FXML file
     */
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

    /**
     * This method is called once the associated button is clicked.
     * The player clicks this button if he wants to load a saved Game.
     * This method loads the right scene and sets the new controller
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during loading the FXML file
     */
    public void loadGameAction(ActionEvent event) throws IOException {
        boolean gameToBeLoad = true;
        String resource = "/it/polimi/ingsw/am42/javafx/GeneralConnection.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        NormalConnectionController normalConnectionController = fxmlLoader.getController();
        normalConnectionController.setClient(ClientHolder.getClient(), gameToBeLoad);
        stage.setScene(scene);
        stage.show();
    }
}