package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.network.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartingController{

    private Client client;
    private Stage stage;
    private Scene scene;
    private String setter;


    public void setClient(Client client){
        this.client = client;
    }

    public void login(ActionEvent e) throws IOException {
        ConnectionState c = client.getGameInfo();
        if(c.equals(ConnectionState.CONNECT)){
            setter = "/it/polimi/ingsw/am42/menu.fxml";
        }
        else if(c.equals(ConnectionState.CREATE)){
            setter = "/it/polimi/ingsw/am42/menu.fxml";
        }
        else{ //(c.equals(ConnectionState.LOAD)){
            setter = "/it/polimi/ingsw/am42/menu.fxml";
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(setter));

        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

    }
}
