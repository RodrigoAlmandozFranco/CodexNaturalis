package it.polimi.ingsw.am42.model.exceptions;

import java.rmi.RemoteException;

/**
 * This exception is thrown when a user tries to place a card but they haven't fulfilled the card's requirements inside their board
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class RequirementsNotMetException extends RemoteException {
    public RequirementsNotMetException(String message) {
        super(message);
    }
}
