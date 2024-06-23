package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.util.Set;
/**
 * Message sent by the server to the client to send the available positions to the client
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class SendAvailablePositionMessage extends Message {

    Set<Position> positions;

    public SendAvailablePositionMessage(Set<Position> pos){
        positions = pos;
    }

    /**
     * This method returns the available Positions
     * @return Set of Positions
     */
    public Set<Position> getPositions() {
        return positions;
    }
}
