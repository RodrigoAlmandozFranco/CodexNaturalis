package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;

public class LobbyController {

    private Client client;
    @FXML
    ProgressBar progressBar;
    @FXML
    Label label;

    public LobbyController() {

    }

    public void setClient(Client client) throws IOException {
        this.client = client;

        Thread thread = new Thread(() -> {
            try {
                askReady();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.start();
    }


    private void askReady() throws IOException {
        while (!client.getView().getReady()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.switchScene();

    }

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
            boardController.setClient(ClientHolder.getClient());

            Scene scene = new Scene(root);

            Stage stage = (Stage)progressBar.getScene().getWindow();

            stage.setScene(scene);

            stage.show();

        });
    }
}