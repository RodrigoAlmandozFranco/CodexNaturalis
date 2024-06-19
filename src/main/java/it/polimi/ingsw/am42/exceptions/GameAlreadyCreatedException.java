package it.polimi.ingsw.am42.exceptions;

import java.rmi.RemoteException;

public class GameAlreadyCreatedException extends RemoteException {
    public GameAlreadyCreatedException(String message) {
        super(message);
    }
}
