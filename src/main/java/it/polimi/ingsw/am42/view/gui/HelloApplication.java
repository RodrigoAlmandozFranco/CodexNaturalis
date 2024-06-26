package it.polimi.ingsw.am42.view.gui;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.view.clientModel.GameClientModel;
import it.polimi.ingsw.am42.view.gui.controller.FirstPlayerCreateGameController;
import it.polimi.ingsw.am42.view.gui.controller.MenuController;
import it.polimi.ingsw.am42.view.gui.controller.NormalConnectionController;
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

/**
 * This class is the main entry point for the JavaFX application.
 * It manages the initial setup of the application, including setting the stage and scene.
 * It also handles the connection state of the client and sets the appropriate scene based on the connection state.
 * This class extends the Application class provided by JavaFX
 *
 * @author Mattia Brandi
 * @author Rodrigo Almandoz Franco
 */

public class HelloApplication extends Application {

    private static Stage stage;

    /**
     * This method sets the right scene based on the ConnectionState, and it
     * starts two different threads to manage the server's disconnection or the player's disconnection
     *
     * @param stage stage where the scene has to be set
     * @throws IOException if an error occurs during the loading process
     */
    @Override
    public void start(Stage stage) throws IOException {
        HelloApplication.stage = stage;

        ConnectionState c = ClientHolder.getClient().getGameInfo();
        String setter;

        if(c.equals(ConnectionState.CREATE)){
            setter = "/it/polimi/ingsw/am42/javafx/NicknameFirstPlayerCreateGame.fxml";
        }
        else if(c.equals(ConnectionState.LOAD)){
            setter = "/it/polimi/ingsw/am42/javafx/FirstPlayerMenu.fxml";
        } else {
            setter = "/it/polimi/ingsw/am42/javafx/GeneralConnection.fxml";
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(setter));
        Scene scene = new Scene(fxmlLoader.load());

        if(c.equals(ConnectionState.LOAD)) {
            MenuController load = fxmlLoader.getController();
            load.setClient(ClientHolder.getClient());
        }
        else if(c.equals(ConnectionState.CREATE)) {
            FirstPlayerCreateGameController first = fxmlLoader.getController();
            first.setClient(ClientHolder.getClient());
        } else {
            NormalConnectionController normal = fxmlLoader.getController();
            normal.setClient(ClientHolder.getClient(), !c.equals(ConnectionState.CONNECT));
        }

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

    /**
     * This method returns the stage
     *
     * @return the stage
     */
    public static Stage getStage() {
        return stage;
    }

    /**
     * This method continues to check if the server is down.
     * If the server is down, it shows an alert and exits the application
     */

    private void checkServerDown() {
        GameClientModel gameClientModel = ClientHolder.getClient().getClientModel();
        boolean gameInProcess = true;
        while (gameInProcess) {
            if (gameClientModel.getServerDown()) {
                gameInProcess = false;
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        if(!gameClientModel.isGameAborted()) {
            Platform.runLater(() -> {
                showAlert("The game has been aborted because of server disconnection.");
                showAlert("The application will now close.");
                System.exit(0);
            });
        }
    }

    /**
     * This method continues to check if a player has disconnected.
     * If the game is aborted, it shows an alert and exits the application.
     */
    private void isGameAborted() {
        GameClientModel gameClientModel = ClientHolder.getClient().getClientModel();
        boolean gameInProcess = true;
        while (gameInProcess) {
            if (gameClientModel.isGameAborted()) {
                gameInProcess = false;
            } else {
                try {
                    Thread.sleep(5000);
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

    /**
     * This method starts the Java FX application
     */
    public void initialize() {
        launch();
    }
}