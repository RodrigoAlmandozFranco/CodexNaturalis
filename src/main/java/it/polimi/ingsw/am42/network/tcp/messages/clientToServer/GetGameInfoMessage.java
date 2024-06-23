package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.ConnectionStateMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.util.Set;

/**
 * Message sent by the client to the server to get the info of the Game
 * The executeServer method calls a controller method in order to get the info of the Game
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class GetGameInfoMessage extends Message{

    public GetGameInfoMessage() {
    }


    /**
     * This method is called by the ClientHandler, and it gets the info of the Game
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message with the current State of the Game
     *
     */
    public Message execute(ClientHandler clientHandler, Controller controller) {
        ConnectionState c =  controller.getGameInfo();
        return new ConnectionStateMessage(c);
    }

}
