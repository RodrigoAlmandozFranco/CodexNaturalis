package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
/**
 * Message sent by the server to the client to notify the updated State of the Game
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ConnectionStateMessage extends Message {
    private ConnectionState state;

    public ConnectionStateMessage(ConnectionState s){
        state = s;
    }

    /**
     * This method gets the State of the Game
     * @return State of the Game
     */
    public ConnectionState getConnectionState(){
        return state;
    }

}
