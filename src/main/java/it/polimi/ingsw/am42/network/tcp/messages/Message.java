package it.polimi.ingsw.am42.network.tcp.messages;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.network.chat.ChatMessage;

import java.io.Serializable;

/**
 * This class represents a generic message that can be sent through the network
 * It is used to send messages between the server and the client
 *
 * @see ServerToClientMessage
 * @see ClientToServerMessage
 * @see ChatMessage
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class Message  implements Serializable {
    public Message() {
    }

    public Message executeServer() {
        return null;
    }

    public void executeClient() {
    }
}

