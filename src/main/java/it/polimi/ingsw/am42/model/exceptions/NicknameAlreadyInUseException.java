package it.polimi.ingsw.am42.model.exceptions;
/**
 * This exception is thrown when the Nickname provided by the user is already used by someone else
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */


public class NicknameAlreadyInUseException extends Exception {
    public NicknameAlreadyInUseException(String message) {
        super(message);
    }
}

