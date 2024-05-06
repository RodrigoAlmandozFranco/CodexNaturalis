package it.polimi.ingsw.am42.network.tcp.messages;

import it.polimi.ingsw.am42.network.tcp.client.ClientTCP;

/**
 * This class is the superclass of all the messages that the server sends to the client
 * It contains a reference to the client that will receive the message
 * @see it.polimi.ingsw.am42.network.tcp.client.ClientTCP
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ServerToClientMessage extends Message  {
    protected ClientTCP client;

    public ServerToClientMessage(ClientTCP c) {
        client = c;
    }

    protected ServerToClientMessage(){}
}
