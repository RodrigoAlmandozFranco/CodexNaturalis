package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendPossibleGoalsMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.WrongTurnErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.util.List;

/**
 * Message sent by the client to the server to choose a color
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ChosenColorMessage extends Message {
    private String nickname;
    private PlayersColor color;

    public ChosenColorMessage(String n, PlayersColor c){
        nickname = n;
        color = c;
    }

    /**
     * This method is called by the ClientHandler, and it sets the color chosen by the player
     * and receives the List of possible Goals
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message that contains the possible Goals or a message to notify a WrongTurnException
     *
     */
    public Message execute(ClientHandler clientHandler, Controller controller) {
        List<GoalCard> goals = null;
        try {
            goals = controller.chooseColor(nickname, color);
        } catch (WrongTurnException e) {
            return new WrongTurnErrorMessage();
        }
        return new SendPossibleGoalsMessage(goals);
    }
}
