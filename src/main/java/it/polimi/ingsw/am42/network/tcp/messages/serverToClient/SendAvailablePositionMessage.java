package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage;

import java.util.Set;

/**
 * Message sent by the server to the client to send the available positions to the client
 * The executeClient method calls a client method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class SendAvailablePositionMessage extends ServerToClientMessage {
    Set<Position> positions;

    public SendAvailablePositionMessage(Set<Position> pos){
        positions = pos;
    }

    public void executeClient(){
        client.getPosition(positions);
    }
}
