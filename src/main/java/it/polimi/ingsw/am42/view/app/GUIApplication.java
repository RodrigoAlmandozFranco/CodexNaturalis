package it.polimi.ingsw.am42.view.app;


import it.polimi.ingsw.am42.view.gui.utils.ClientHolder;
import it.polimi.ingsw.am42.view.gui.HelloApplication;
import it.polimi.ingsw.am42.network.Client;

/**
 * This class is responsible for starting the graphical user interface of the application.
 * It extends the App class and sets up the HelloApplication.
 * It uses the ClientHolder to provide a global point of access to the Client object
 *
 * @author Mattia Brandi
 * @author Rodrigo Almandoz Franco
 */

public class GUIApplication extends App {
    private HelloApplication helloApplication;


    public GUIApplication(Client client) {
        super(client);
        ClientHolder.setClient(client);

        //this.client = client;
    }

    /**
     * This method starts and initializes the HelloApplication
     */
    public void start() {
        helloApplication = new HelloApplication();
        helloApplication.initialize();
    }
}

