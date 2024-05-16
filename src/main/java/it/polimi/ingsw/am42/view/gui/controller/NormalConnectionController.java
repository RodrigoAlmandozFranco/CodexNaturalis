package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NormalConnectionController {

    private Client client;
    private String nickname = "";

    @FXML
    TextField textField;

    public NormalConnectionController() {}

    public void setClient(Client client) {
        this.client = client;
    }

    public void submit() {
        this.nickname = textField.getText().trim();
    }


    public void connectGameAction(ActionEvent event) throws IOException {
        if(nickname.trim().isEmpty()) {
            showAlert("Please insert a nickname");
        } else {
            connect(event);
        }
    }

    private void connect(ActionEvent event) throws IOException {
        try {
            if(client.getGameInfo().equals(ConnectionState.CONNECT))
                client.connect(nickname);
            else
                client.connectAfterLoad(nickname);
            load(event);
        } catch (GameFullException e) {
            showAlert("The game is full");
            showAlert("Closing the game...");
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
            System.exit(1);
        } catch (NicknameAlreadyInUseException e) {
            showAlert("Nickname already in use");
        } catch (NicknameInvalidException e) {
            showAlert("Invalid nickname");
        }
    }

    private void load(ActionEvent event) throws IOException {
        String resource = "/it/polimi/ingsw/am42/javafx/Board.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Parent root = fxmlLoader.load();
        //BoardController boardController = fxmlLoader.getController();
        //boardController.setClient(ClientHolder.getClient());

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
