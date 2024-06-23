package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.network.tcp.messages.Message;
/**
 * Message sent by the server to the client to notify that the chosen number of players is wrong.
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class NumberPlayersWrongErrorMessage extends Message {
    private String message;
    public NumberPlayersWrongErrorMessage(String message){
        this.message = message;
    }

    /**
     * This method gets the message
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
