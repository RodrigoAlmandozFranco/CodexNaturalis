package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage;

/**
 * Message sent by the server to the client to notify that the nickname is already in use.
 * The executeClient method calls a client method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class NicknameAlreadyInUseErrorMessage extends ServerToClientMessage {

    public NicknameAlreadyInUseErrorMessage(){

    }

    public void executeClient(){
        client.getNickname();
    }
}
