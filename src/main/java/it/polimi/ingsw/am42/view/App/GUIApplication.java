package it.polimi.ingsw.am42.view.App;


import it.polimi.ingsw.am42.view.gui.controller.ClientHolder;
import it.polimi.ingsw.am42.view.gui.controller.HelloApplication;
import it.polimi.ingsw.am42.network.Client;



public class GUIApplication extends App {
    private HelloApplication helloApplication;


    public GUIApplication(Client client) {
        super(client);
        ClientHolder.setClient(client);

        //this.client = client;
    }

    public void start() {
        helloApplication = new HelloApplication();
        helloApplication.initialize();

    }



}

