package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage;

import java.util.List;

/**
 * Message sent by the server to the client to send the winner(s) of the game
 * The executeClient method calls a client method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */


public class SendWinnerMessage extends ServerToClientMessage {
    private List<Player> winners;

    public SendWinnerMessage(List<Player> w){
        winners = w;
    }

    public void executeClient() {
        client.showWinner(winners);
    }
}
