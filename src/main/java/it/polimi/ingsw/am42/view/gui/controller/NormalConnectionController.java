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

    public void setClient(Client client, boolean gameToBeLoad) {
        this.client = client;
        LoginButton1.setOnMouseEntered(event -> LoginButton1.setCursor(Cursor.HAND));
        LoginButton1.setOnMouseExited(event -> LoginButton1.setCursor(Cursor.DEFAULT));
        this.gameToBeLoad = gameToBeLoad;
    }

    public void submit() {
        this.nickname = textField.getText().trim();
    }


    public void connectGameAction(ActionEvent event) throws IOException {
        if(nickname.trim().isEmpty() && textField.getText().trim().isEmpty()){
            showAlert("Please insert a nickname");
        } else {
            nickname = textField.getText().trim();
            connect(event);
        }
    }


    private void connectAction(ActionEvent event) throws IOException {

        try {
            client.connect(nickname);
            isGameToLoad = false;
            client.getView().setNickname(nickname);
            load(event);
        } catch (GameFullException e) {
            gameFull(e, event);
        } catch (NicknameInvalidException | NicknameAlreadyInUseException e) {
            showAlert(e.getMessage());
        }
    }

    private void reconnectAction(ActionEvent event) throws IOException {
        try {
            client.reconnect(nickname);
            isGameToLoad = true;
            client.getView().setNickname(nickname);
            load(event);
        } catch (GameFullException e) {
            gameFull(e, event);
        } catch (NicknameInvalidException | NicknameAlreadyInUseException e) {
            showAlert(e.getMessage());
        } catch (GameAlreadyCreatedException e) {
            gameAlreadyCreated(e, event);
        }
    }

    private void gameFull(GameFullException e, ActionEvent event){
        showAlert(e.getMessage());
        showAlert("Closing the game...");
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(1);
    }

    private void gameAlreadyCreated(GameAlreadyCreatedException e, ActionEvent event) throws IOException {
        showAlert("Game has already been created. We are connecting you to the active game...");
        connectAction(event);
    }

    private void connect(ActionEvent event) throws IOException {
        if(!gameToBeLoad){
            connectAction(event);
        } else {
            reconnectAction(event);
        }
    }

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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

