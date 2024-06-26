package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GameAlreadyCreatedErrorMessage;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;

/**
 * This class represents the GUI controller that controls the connection phase,
 * during which the Player has to authenticate by choosing his nickname
 *
 * @author Mattia Brandi
 * @author Rodrigo Almandoz Franco
 */
public class NormalConnectionController {

    private Client client;
    private String nickname = "";
    @FXML
    private Button LoginButton1;
    private boolean isGameToLoad;
    private boolean gameToBeLoad;

    @FXML
    TextField textField;

    public NormalConnectionController() {}

    /**
     * This method sets the Client in the Controller, and it sets the cursor's shape
     * based on its position on the screen
     *
     * @param client client instance
     * @param gameToBeLoad flag that indicates if the player is connecting to a new game or to a loaded game
     */
    public void setClient(Client client, boolean gameToBeLoad) {
        this.client = client;
        LoginButton1.setOnMouseEntered(event -> LoginButton1.setCursor(Cursor.HAND));
        LoginButton1.setOnMouseExited(event -> LoginButton1.setCursor(Cursor.DEFAULT));
        this.gameToBeLoad = gameToBeLoad;
    }

    /**
     * This method extracts the user's name from the JavaFx text box
     */
    public void submit() {
        this.nickname = textField.getText().trim();
    }

    /**
     * This method is called once the button is clicked.
     * It verifies that all the fields are correctly filled, and it starts the connection process
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during the connection process
     */
    public void connectGameAction(ActionEvent event) throws IOException {
        if (nickname.trim().isEmpty() && textField.getText().trim().isEmpty()) {
            showAlert("Please insert a nickname");
        } else {
            nickname = textField.getText().trim();
            connect(event);
        }
    }

    /**
     * This method tries to send to the server the request to connect the player to the Game,
     * and it sets the nickname in the GameClientModel.
     * At the end it switches the scene
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during the connection process
     */
    private void connectAction(ActionEvent event) throws IOException {

        try {
            client.connect(nickname);
            isGameToLoad = false;
            client.getClientModel().setNickname(nickname);
            load(event);
        } catch (GameFullException e) {
            gameFull(e, event);
        } catch (NicknameInvalidException | NicknameAlreadyInUseException e) {
            showAlert(e.getMessage());
        }
    }

    /**
     * This method tries to send to the server the request to reconnect the player to the loaded Game,
     * and it sets the nickname in the GameClientModel.
     * At the end it switches the scene
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during the reconnection process
     */
    private void reconnectAction(ActionEvent event) throws IOException {
        try {
            client.reconnect(nickname);
            isGameToLoad = true;
            client.getClientModel().setNickname(nickname);
            load(event);
        } catch (GameFullException e) {
            gameFull(e, event);
        } catch (NicknameInvalidException | NicknameAlreadyInUseException e) {
            showAlert(e.getMessage());
        } catch (GameAlreadyCreatedException e) {
            gameAlreadyCreated(e, event);
        }
    }

    /**
     * This method shows an alert with the exception message, and it closes the window
     *
     * @param e GameFullException thrown by the server
     * @param event ActionEvent triggered when arrives an exception
     */
    private void gameFull(GameFullException e, ActionEvent event){
        showAlert(e.getMessage());
        showAlert("Closing the game...");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(1);
    }

    /**
     * This method shows an alert with the exception message
     *
     * @param e GameAlreadyCreatedException thrown by the server
     * @param event ActionEvent triggered when arrives an exception
     * @throws IOException if an error occurs during the connection process
     */
    private void gameAlreadyCreated(GameAlreadyCreatedException e, ActionEvent event) throws IOException {
        showAlert("Game has already been created. We are connecting you to the active game...");
        connectAction(event);
    }

    /**
     * This method calls the right method in order to do a normal connection or a reconnection
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during the connection process
     */
    private void connect(ActionEvent event) throws IOException {
        if (!gameToBeLoad && !client.getGameInfo().equals(ConnectionState.LOADING)) {
            connectAction(event);
        } else {
            reconnectAction(event);
        }
    }

    /**
     * This method creates a new Scene loading the FXML file, and it sets a new Controller
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during the loading process
     */
    private void load(ActionEvent event) throws IOException {
        String resource = "/it/polimi/ingsw/am42/javafx/Lobby.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Parent root = fxmlLoader.load();
        LobbyController lobbycontroller = fxmlLoader.getController();
        lobbycontroller.setClient(ClientHolder.getClient(), isGameToLoad);

        Scene scene = new Scene(root);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * This method shows an alert once called
     *
     * @param message message of the alert
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

