package it.polimi.ingsw.am42.network.tcp.messages;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.network.chat.ChatMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.io.Serializable;

/**
 * This class represents a generic message that can be sent through the network
 * It is used to send messages between the server and the client
 *
 * @see ChatMessage
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class Message  implements Serializable {
    public Message() {
    }

    /**
     * This method is meant to be overridden by all the messages sent by the client to the server
     * @param clientHandler
     * @param controller
     * @return null
     */
    public Message execute(ClientHandler clientHandler, Controller controller) {
        return null;
    }

}

