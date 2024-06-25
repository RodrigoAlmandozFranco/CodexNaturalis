package it.polimi.ingsw.am42.exceptions;

import java.rmi.RemoteException;

/**
 * This exception is used to notify the player in login mode that there is a game already created.
 * @see it.polimi.ingsw.am42.controller.Controller
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class GameAlreadyCreatedException extends RemoteException {
    public GameAlreadyCreatedException(String message) {
        super(message);
    }
}
