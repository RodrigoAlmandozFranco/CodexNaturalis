package it.polimi.ingsw.am42.view.gui;

import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gameview.GameView;
import it.polimi.ingsw.am42.view.gui.controller.StartingController;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    private static Stage stage;


    @Override
    public void start(Stage stage) throws IOException {
        HelloApplication.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/it/polimi/ingsw/am42/javafx/Starting.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        StartingController startingController = fxmlLoader.getController();
        startingController.setClient(ClientHolder.getClient());

        new Thread(this::isGameAborted).start();
        new Thread(this::checkServerDown).start();


        stage.setTitle("Codex Naturalis");
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/graphical/CodexNaturalis.png")));
        stage.getIcons().add(icon);

        stage.setOnCloseRequest(event -> {
            System.exit(1);
        });


        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getStage() {
        return stage;
    }

    private void checkServerDown() {
        GameView gameView = ClientHolder.getClient().getView();
        boolean gameInProcess = true;
        while (gameInProcess) {
            if (gameView.getServerDown()) {
                gameInProcess = false;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Platform.runLater(() -> {
            showAlert("The game has been aborted because of server disconnection.");
            showAlert("The application will now close.");
            System.exit(0);
        });
    }

    private void isGameAborted() {
        GameView gameView = ClientHolder.getClient().getView();
        boolean gameInProcess = true;
        while (gameInProcess) {
            if (gameView.isGameAborted()) {
                gameInProcess = false;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        Platform.runLater(() -> {
            showAlert("The game has been aborted because of a player disconnection.");
            showAlert("The application will now close.");
            System.exit(0);
        });
    }

    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void initialize() {
        launch();
    }
}