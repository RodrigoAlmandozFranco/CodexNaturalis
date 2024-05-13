package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;

/**
 * Message sent by the client to the server to choose the personal goal
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ChosenGoalMessage extends ClientToServerMessage {
    private String nickname;
    private GoalCard goal;

    public ChosenGoalMessage(String n, GoalCard g){
        nickname = n;
        goal = g;
    }

    public Message execute(Controller controller) {
        controller.chooseGoal(nickname, goal);
        return new GoodMessage();
    }
}

