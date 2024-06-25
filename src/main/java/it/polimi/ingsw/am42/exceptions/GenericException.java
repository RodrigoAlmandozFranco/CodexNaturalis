package it.polimi.ingsw.am42.exceptions;

/**
 * This exception is used in order to indicate the main class of possible malfunctions in Server
 * @see it.polimi.ingsw.am42.CodexNaturalis
 * @see it.polimi.ingsw.am42.network.Server
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class GenericException extends Exception{
    public GenericException(String message) {
        super(message);
    }
}
