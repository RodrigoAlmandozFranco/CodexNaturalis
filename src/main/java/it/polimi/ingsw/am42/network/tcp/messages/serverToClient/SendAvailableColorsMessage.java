package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.util.List;
/**
 * Message sent by the server to the client to send the available colors to the client
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class SendAvailableColorsMessage extends Message {

    private List<PlayersColor> colors;

    public SendAvailableColorsMessage(List<PlayersColor> c){
        colors = c;
    }

    /**
     * This method returns the list of the available Colors
     * @return List of Colors
     */
    public List<PlayersColor> getColors() {
        return colors;
    }
}
