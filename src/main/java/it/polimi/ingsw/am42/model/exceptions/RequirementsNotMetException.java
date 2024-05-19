package it.polimi.ingsw.am42.model.exceptions;

import java.rmi.RemoteException;

public class RequirementsNotMetException extends RemoteException {
    public RequirementsNotMetException(String message) {
        super(message);
    }
}
