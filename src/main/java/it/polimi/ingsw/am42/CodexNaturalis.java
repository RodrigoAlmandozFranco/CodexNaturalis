package it.polimi.ingsw.am42;

import it.polimi.ingsw.am42.exceptions.GenericException;
import it.polimi.ingsw.am42.network.Server;

public class CodexNaturalis {
    public static void main(String[] args) throws GenericException {
        for (String arg : args) {
            if (arg.equals("--server") || arg.equals("-s")) {
                Server.main(args);
            } else if(arg.equals("--client") || arg.equals("-c")) {
                ClientMain.main(args);
            }
        }
    }
}
