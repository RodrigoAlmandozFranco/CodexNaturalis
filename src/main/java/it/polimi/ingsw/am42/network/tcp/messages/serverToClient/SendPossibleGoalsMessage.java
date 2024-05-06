package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage;

import java.util.List;

/**
 * Message sent by the server to the client to send the possible goals to choose from
 * The executeClient method calls a client method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class SendPossibleGoalsMessage extends ServerToClientMessage {
    private List<GoalCard> goals;

    public SendPossibleGoalsMessage(List<GoalCard> g){
        goals = g;
    }

    public void executeClient(){
        client.chooseGoal(goals);
    }
}
