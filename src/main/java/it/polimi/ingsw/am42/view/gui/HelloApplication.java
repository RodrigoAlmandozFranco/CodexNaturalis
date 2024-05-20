package it.polimi.ingsw.am42.view.gui;

import it.polimi.ingsw.am42.view.gui.controller.StartingController;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/it/polimi/ingsw/am42/javafx/Starting.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        StartingController startingController = fxmlLoader.getController();
        startingController.setClient(ClientHolder.getClient());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        launch();
    }
}