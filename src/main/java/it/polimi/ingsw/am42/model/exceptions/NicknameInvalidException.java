package it.polimi.ingsw.am42.model.exceptions;

/**
 * This exception is thrown when a user gives an invalid nickname
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class NicknameInvalidException extends Exception {
    public NicknameInvalidException(String message) {
        super(message);
    }
}

