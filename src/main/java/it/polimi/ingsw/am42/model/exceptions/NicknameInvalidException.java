package it.polimi.ingsw.am42.model.exceptions;

import java.rmi.RemoteException;

/**
 * This exception is thrown when a user gives an invalid nickname
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class NicknameInvalidException extends RemoteException {
    public NicknameInvalidException(String message) {
        super(message);
    }
}

