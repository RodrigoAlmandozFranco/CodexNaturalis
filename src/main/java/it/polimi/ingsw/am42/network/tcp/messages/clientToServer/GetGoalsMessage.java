package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendPossibleGoalsMessage;

import java.util.List;

/**
 * Message sent by the client to the server to get the possible personal goals
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class GetGoalsMessage extends ClientToServerMessage {
    private String nickname;

    public GetGoalsMessage(String nickname) {
        this.nickname = nickname;
    }

    public Message execute() {
        List<GoalCard> goals = controller.getGoals(nickname);
        return new SendPossibleGoalsMessage(goals);
    }
}
