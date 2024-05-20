package it.polimi.ingsw.am42.model.exceptions;

import java.rmi.RemoteException;

/**
 * This exception is thrown when the Nickname provided by the user is already used by someone else
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */


public class NicknameAlreadyInUseException extends RemoteException {
    public NicknameAlreadyInUseException(String message) {
        super(message);
    }
}

