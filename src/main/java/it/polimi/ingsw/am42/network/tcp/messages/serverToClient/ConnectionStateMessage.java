package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.controller.ConnectionState;
import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

public class ConnectionStateMessage extends Message {
    private ConnectionState state;

    public ConnectionStateMessage(ConnectionState s){
        state = s;
    }

    public ConnectionState getConnectionState(){
        return state;
    }

}
