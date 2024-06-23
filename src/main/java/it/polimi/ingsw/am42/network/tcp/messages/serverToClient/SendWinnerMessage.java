package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.util.List;
/**
 * Message sent by the server to the client to send the winner(s) of the game
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class SendWinnerMessage extends Message {
    private List<Player> winners;

    public SendWinnerMessage(List<Player> w){
        winners = w;
    }

    /**
     * This method returns the winner(s)
     * @return List of winner(s)
     */
    public List<Player> getWinners() {
        return winners;
    }
}
