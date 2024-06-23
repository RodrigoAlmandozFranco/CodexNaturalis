package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.controller.gameDB.Change;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
/**
 * Message sent by the server to the client to send the Change object that contains the updated info
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class ChangeMessage extends Message {
    private Change change;

    public ChangeMessage(Change c){
        change = c;
    }

    /**
     * This method gets the Change
     * @return Change
     */
    public Change getChange(){
        return change;
    }

}
