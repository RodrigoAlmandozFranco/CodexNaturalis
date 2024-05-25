package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.network.tcp.messages.Message;

/**
 * Message sent by the server to the client to notify that the game is full.
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
*/


public class GameFullErrorMessage extends Message {
    String message;
    public GameFullErrorMessage(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
