package it.polimi.ingsw.am42.model.exceptions;

/**
 * This exception is thrown when a user tries to join a game that is already full
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class GameFullException extends Exception {
    public GameFullException(String message) {
        super(message);
    }
}
