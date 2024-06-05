package it.polimi.ingsw.am42;

import it.polimi.ingsw.am42.exceptions.GenericException;
import it.polimi.ingsw.am42.network.Server;
import it.polimi.ingsw.am42.view.ClientMain;

/**
 * Main class for the project
 * It will start the server or the client based on the arguments
 * @author Rodrigo Almandoz Franco
 */
public class CodexNaturalis {
    public static void main(String[] args) {
        for (String arg : args) {
            if (arg.equals("--server") || arg.equals("-s")) {
                try {
                    Server.main(args);
                } catch (GenericException e) {
                    e.printStackTrace();
                }
            } else if(arg.equals("--client") || arg.equals("-c")) {
                ClientMain.main(args);
            }
        }
    }
}
