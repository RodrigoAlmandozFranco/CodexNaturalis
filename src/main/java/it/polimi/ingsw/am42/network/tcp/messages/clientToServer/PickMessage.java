package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendAvailableColorsMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.WrongTurnErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.util.List;

/**
 * Message sent by the client to the server to pick a card
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class PickMessage extends Message {
    private String nickname;
    private PlayableCard card;

    public PickMessage(String n, PlayableCard c){
        nickname = n;
        card = c;
    }

    /**
     * This method is called by the ClientHandler, and it calls the controller to pick the chosen Card
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message to notify that everything has been done correctly of a message to notify a WrongTurnException
     *
     */
    @Override
    public Message execute(ClientHandler clientHandler, Controller controller) {
        try {
            controller.pick(nickname, card);
            return new GoodMessage();
        } catch (WrongTurnException e) {
            return new WrongTurnErrorMessage();
        }
    }
}
