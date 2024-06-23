package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GameAlreadyCreatedErrorMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.WrongTurnErrorMessage;
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

    /**
     * This method is called by the ClientHandler, and it sets the Goal chosen by the player on the server
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message to notify that everything has been done correctly or a message to notify a WrongTurnException
     *
     */
    public Message execute(ClientHandler clientHandler, Controller controller) {
        try {
            controller.chooseGoal(nickname, goal);
            return new GoodMessage();
        } catch (WrongTurnException e) {
            return new WrongTurnErrorMessage();
        }

    }
}

