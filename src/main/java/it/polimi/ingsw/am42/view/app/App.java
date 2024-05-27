package it.polimi.ingsw.am42.view.app;

import it.polimi.ingsw.am42.network.Client;

public abstract class App {

    protected static Client client;


    public App(Client client) {
        this.client = client;
    }

    // Only for testing!!!
    public App() {

    }

    public void start() {}

}
