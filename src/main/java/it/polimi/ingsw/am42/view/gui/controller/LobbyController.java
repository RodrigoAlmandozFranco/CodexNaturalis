package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * This class represents the GUI controller that controls the Lobby,
 * where the connected players wait until the reaching of the number chosen by
 * the first player
 *
 * @author Mattia Brandi
 * @author Rodrigo Almandoz Franco
 */
public class LobbyController {

    private Client client;
    private boolean gameToLoad;
    @FXML
    ProgressBar progressBar;
    @FXML
    Label label;

    public LobbyController() {

    }

    /**
     * This method sets the Client, and it waits until the number of waiting players
     * is equal to the number chosen by the first player
     *
     * @param client client instance
     * @param gameToLoad flag to understand if there is a game to be loaded
     * @throws IOException if an error occurs during the waiting process
     */
    public void setClient(Client client, boolean gameToLoad) throws IOException {
        this.client = client;
        this.gameToLoad = gameToLoad;
        Thread thread = new Thread(() -> {
            try {
                askReady();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }
    /**
     * This method continues to ask the server if the number of waiting players
     * is equal to the number chosen by the first player, and then it switches to
     * the following scene
     *
     * @throws IOException if an error occurs when the client communicates with the server
     */
    private void askReady() throws IOException {
        while (!client.getClientModel().getReady()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.switchScene();
    }

    /**
     * This method switches the Scene on the JavaFx stage, and it changes the controller,
     * loading it from the fxmlLoader
     *
     * @throws IOException if an error occurs during the switching process
     */
    public void switchScene() throws IOException {
        Platform.runLater(() -> {
            String resource = "/it/polimi/ingsw/am42/javafx/Board.fxml";

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resource));
            Parent root = null;
            try {
                root = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            BoardController boardController = fxmlLoader.getController();
            boardController.setClient(ClientHolder.getClient(), gameToLoad);

            Scene scene = new Scene(root);

            Stage stage = (Stage)progressBar.getScene().getWindow();

            stage.setScene(scene);

            stage.show();

        });
    }
}