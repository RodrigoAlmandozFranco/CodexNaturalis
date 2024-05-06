package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;

public class ChangeMessage extends ClientToServerMessage {
    private Change change;

    public ChangeMessage(Change c){
        change = c;
    }

    public Change getChange(){
        return change;
    }

}
