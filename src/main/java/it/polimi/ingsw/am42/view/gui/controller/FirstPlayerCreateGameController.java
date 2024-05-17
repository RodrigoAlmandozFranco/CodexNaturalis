package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class FirstPlayerCreateGameController implements Initializable {
    private Client client;
    private String nickname = "";
    private int numberOfPlayers = 0;
    @FXML
    private ChoiceBox<String> choiceBox;

    private String[] numbers = {"2","3","4"};

    @FXML
    TextField textField;

    public FirstPlayerCreateGameController() {}

    public void setClient(Client client) {
        this.client = client;
    }

    public void submit() {
        this.nickname = textField.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(numbers);
        choiceBox.setOnAction(this::getNumber);
    }

    public void getNumber(ActionEvent event) {
        numberOfPlayers = Integer.parseInt(choiceBox.getValue());
    }

    public void createGameAction(ActionEvent event) throws IOException {

        if(numberOfPlayers == 0 && nickname.trim().isEmpty()) {
            showAlert("Please select the number of players and your nickname");
        } else if(numberOfPlayers == 0) {
            showAlert("Please select the number of players");
        } else if(nickname.trim().isEmpty()) {
            showAlert("Please insert a nickname");
        } else {
            connect(event);
        }
    }

    private void connect(ActionEvent event) throws IOException {
        try {
            client.createGame(nickname, numberOfPlayers);
            client.getView().setMyNickname(nickname);
        } catch (Exception exception) {
            showAlert("Error creating game");
            System.exit(1);
        }

        String resource = "/it/polimi/ingsw/am42/javafx/Lobby.fxml";

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        Parent root = fxmlLoader.load();
        LobbyController lobbycontroller = fxmlLoader.getController();
        lobbycontroller.setClient(ClientHolder.getClient());

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
