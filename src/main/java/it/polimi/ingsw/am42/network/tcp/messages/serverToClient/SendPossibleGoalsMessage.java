package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.util.List;

/**
 * Message sent by the server to the client to send the possible goals to choose from
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class SendPossibleGoalsMessage extends Message {
    private List<GoalCard> goals;

    public SendPossibleGoalsMessage(List<GoalCard> g){
        goals = g;
    }

    public List<GoalCard> getGoals() {
        return goals;
    }
}

