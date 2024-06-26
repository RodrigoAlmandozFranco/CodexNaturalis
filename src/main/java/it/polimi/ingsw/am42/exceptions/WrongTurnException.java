package it.polimi.ingsw.am42.exceptions;


import java.rmi.RemoteException;


/**
 * Exception for when a client requests to do an illegal action
 *
 * @author TUTTI
 */
public class WrongTurnException extends RemoteException {
    public WrongTurnException(String message) {
        super(message);
    }
}
