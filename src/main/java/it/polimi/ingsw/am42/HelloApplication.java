package it.polimi.ingsw.am42;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/it/polimi/ingsw/am42/menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/polimi/ingsw/am42/graphical/gameStructure/codexNaturalis.png")));
        stage.getIcons().add(icon);
        stage.setTitle("CODEX NATURALIS");
        stage.setWidth(976);
        stage.setHeight(720);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}


