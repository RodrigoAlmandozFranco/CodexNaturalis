package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class represents the GUI controller that controls the setup phase,
 * during which the first Player has to authenticate by choosing his nickname and the
 * number of players of the Game
 *
 * @author Mattia Brandi
 * @author Rodrigo Almandoz Franco
 */
public class FirstPlayerCreateGameController implements Initializable {
    private Client client;
    private String nickname = "";
    private int numberOfPlayers = 0;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Button LoginButton1;

    private String[] numbers = {"2","3","4"};

    @FXML
    TextField textField;

    public FirstPlayerCreateGameController() {}

    /**
     * This method sets the Client in the Controller, and it sets the cursor shape
     * based on its position on the screen
     *
     * @param client client instance
     */
    public void setClient(Client client) {
        this.client = client;
        LoginButton1.setOnMouseEntered(event -> LoginButton1.setCursor(Cursor.HAND));
        LoginButton1.setOnMouseExited(event -> LoginButton1.setCursor(Cursor.DEFAULT));
    }

    /**
     * This method extracts the user's name from the JavaFx text box
     */
    public void submit() {
        this.nickname = textField.getText();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(numbers);
        choiceBox.setOnAction(this::getNumber);
    }

    /**
     * This method is called once the button is clicked.
     * This method extracts the number of players from the JavaFx choice box
     *
     * @param event ActionEvent triggered when the client selects a value
     */
    public void getNumber(ActionEvent event) {
        numberOfPlayers = Integer.parseInt(choiceBox.getValue());
    }

    /**
     * This method is called once the button is clicked.
     * This method verifies that all the fields are correctly filled, and it sends to the
     * server the request to create the game
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during the connection process
     */
    public void createGameAction(ActionEvent event) throws IOException {

        if(numberOfPlayers == 0 && nickname.trim().isEmpty() && textField.getText().trim().isEmpty()) {
            showAlert("Please select the number of players and your nickname");
        } else if(numberOfPlayers == 0) {
            showAlert("Please select the number of players");
        } else if(nickname.trim().isEmpty() && textField.getText().trim().isEmpty()) {
            showAlert("Please insert a nickname");
        } else {
            if(nickname.trim().isEmpty())
                nickname = textField.getText().trim();
            connect(event);
        }
    }

    /**
     * This method is called once the button is clicked.
     * It asks the server to create the Game with the chosen parameters, and
     * it sets the new Scene with the new Controller on the JavaFx stage based on
     * the server's answer
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @throws IOException if an error occurs during the loading process
     */
    private void connect(ActionEvent event) throws IOException {
        String resource = "";

        try {
            client.createGame(nickname, numberOfPlayers);
            client.getClientModel().setNickname(nickname);
            resource = "/it/polimi/ingsw/am42/javafx/Lobby.fxml";
            load(resource, event);
        } catch (GameAlreadyCreatedException e) {
            showAlert("Game already created. We are redirecting you to the connection page. Please be patient.");
            resource = "/it/polimi/ingsw/am42/javafx/GeneralConnection.fxml";
            load(resource, event);
        }
        catch (Exception exception) {
            showAlert(exception.getMessage());
            System.exit(1);
        }
    }

    /**
     * This method creates a new Scene loading the FXML file, and it sets a new Controller
     * based on the input resource
     *
     * @param event ActionEvent triggered when the client clicks the button
     * @param resource path to the FXM file
     * @throws IOException if an error occurs during the loading process
     */
    private void load(String resource, ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader;
        Parent root;
        fxmlLoader = new FXMLLoader(getClass().getResource(resource));
        root = fxmlLoader.load();

        switch (resource) {
            case "/it/polimi/ingsw/am42/javafx/Lobby.fxml" -> {
                LobbyController lobbycontroller = fxmlLoader.getController();
                lobbycontroller.setClient(ClientHolder.getClient(), false);
            }
            case "/it/polimi/ingsw/am42/javafx/GeneralConnection.fxml" -> {
                NormalConnectionController normalConnectionController = fxmlLoader.getController();
                normalConnectionController.setClient(ClientHolder.getClient(), false);
            }
        }

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