package it.polimi.ingsw.am42.view.app;


import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import it.polimi.ingsw.am42.view.gui.HelloApplication;
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

