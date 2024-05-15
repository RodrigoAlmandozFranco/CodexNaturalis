package it.polimi.ingsw.am42.view.gui.controller;

import it.polimi.ingsw.am42.network.Client;

public class ClientHolder {
    private static Client client;

    public static void setClient(Client client){
        ClientHolder.client = client;
    }

    public static Client getClient(){
        return client;
    }

}
