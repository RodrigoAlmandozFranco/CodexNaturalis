package it.polimi.ingsw.am42.model.exceptions;

/**
 * This exception is thrown when a user gives an invalid number of players
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class NumberPlayerWrongException extends Exception {
    public NumberPlayerWrongException(String message) {
        super(message);
    }
}
