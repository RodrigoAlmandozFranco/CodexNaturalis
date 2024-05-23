package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

/**
 * Message sent by the client to the server to choose the personal goal
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ChosenGoalMessage extends Message {
    private String nickname;
    private GoalCard goal;

    public ChosenGoalMessage(String n, GoalCard g){
        nickname = n;
        goal = g;
    }

    public Message execute(ClientHandler clientHandler, Controller controller) {
        controller.chooseGoal(nickname, goal);
        //return null;
        return new GoodMessage();
    }
}

