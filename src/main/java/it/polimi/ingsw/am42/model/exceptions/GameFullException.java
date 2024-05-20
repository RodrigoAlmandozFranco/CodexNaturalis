package it.polimi.ingsw.am42.model.exceptions;

import java.rmi.RemoteException;

/**
 * This exception is thrown when a user tries to join a game that is already full
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class GameFullException extends RemoteException {
    public GameFullException(String message) {
        super(message);
    }
}
