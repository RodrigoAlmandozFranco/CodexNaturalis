package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage;

/**
 * Message sent by the server to the client to notify that the game is full.
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
*/


public class GameFullErrorMessage extends ServerToClientMessage {

    public GameFullErrorMessage(){
    }

    public void executeClient(){
        client.gameFull();
    }
}
