package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.ConnectionStateMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.util.Set;

public class GetGameInfoMessage extends Message{

    public GetGameInfoMessage() {
    }


    public Message execute(ClientHandler clientHandler, Controller controller) {
        ConnectionState c =  controller.getGameInfo();
        return new ConnectionStateMessage(c);
    }

}
