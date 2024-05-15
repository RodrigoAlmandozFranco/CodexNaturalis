package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.network.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/it/polimi/ingsw/am42/menu.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/graphical/gameStructure/codexNaturalis.png")));
//        stage.getIcons().add(icon);
//        stage.setTitle("CODEX NATURALIS");
//        stage.setWidth(976);
//        stage.setHeight(720);
//        stage.setResizable(false);
//        stage.setScene(scene);
//        stage.show();
          FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/it/polimi/ingsw/am42/starting.fxml"));
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


