package it.polimi.ingsw.am42.view.gui.utils;

import it.polimi.ingsw.am42.network.Client;

/**
 * This class is a utility class that holds a static reference to the Client object.
 * This class provides a global point of access to the Client object
 */
public class ClientHolder {
    private static Client client;

    /**
     * This method sets the Client object
     *
     * @param client client instance
     */
    public static void setClient(Client client){
        ClientHolder.client = client;
    }

    /**
     * This method returns the Client object
     *
     * @return client object held by this class
     */
    public static Client getClient(){
        return client;
    }

}
