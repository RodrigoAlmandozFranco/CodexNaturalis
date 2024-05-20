package it.polimi.ingsw.am42.model.exceptions;

import java.rmi.RemoteException;

/**
 * This exception is thrown when a user gives an invalid number of players
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class NumberPlayerWrongException extends RemoteException {
    public NumberPlayerWrongException(String message) {
        super(message);
    }
}
