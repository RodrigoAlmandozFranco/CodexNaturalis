
package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.network.Client;
import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class StartingController{

    private Client client;
    private Stage stage;
    private Scene scene;
    private String setter;

    @FXML
    Button loginButton;

    public StartingController() {


    }


    public void setClient(Client client){
        this.client = client;
        loginButton.setOnMouseEntered(event -> loginButton.setCursor(Cursor.HAND));
        loginButton.setOnMouseExited(event -> loginButton.setCursor(Cursor.DEFAULT));
    }

    public void login(ActionEvent e) throws IOException {
        ConnectionState c = client.getGameInfo();

        if(c.equals(ConnectionState.CREATE)){
            setter = "/it/polimi/ingsw/am42/javafx/NicknameFirstPlayerCreateGame.fxml";
        }
        else if(c.equals(ConnectionState.CONNECT)){
            setter = "/it/polimi/ingsw/am42/javafx/GeneralConnection.fxml";
        }
        else if(c.equals(ConnectionState.LOAD)){
            setter = "/it/polimi/ingsw/am42/javafx/FirstPlayerMenu.fxml";
        } else {
            setter = "/it/polimi/ingsw/am42/javafx/GeneralConnection.fxml";
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(setter));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);

        if(c.equals(ConnectionState.CONNECT)) {
            NormalConnectionController normal = fxmlLoader.getController();
            normal.setClient(ClientHolder.getClient());
        }
        else if(c.equals(ConnectionState.LOAD)) {
            MenuController load = fxmlLoader.getController();
            load.setClient(ClientHolder.getClient());
        }
        else if(c.equals(ConnectionState.CREATE)) {
            FirstPlayerCreateGameController first = fxmlLoader.getController();
            first.setClient(ClientHolder.getClient());
        }

        stage.show();

    }
}
