package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendAvailableColorsMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendWinnerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.WrongTurnErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.util.List;

/**
 * Message sent by the client to the server to get the winner
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class GetWinnerMessage extends Message {

    public GetWinnerMessage() {}

    /**
     * This method is called by the ClientHandler and gets the list of winners
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message with the list of winners or a message to notify a WrongTurnException
     *
     */
    @Override
    public Message execute(ClientHandler clientHandler, Controller controller) {
        try {
            List<Player> winners = controller.getWinner();
            return new SendWinnerMessage(winners);
        } catch (WrongTurnException e) {
            return new WrongTurnErrorMessage();
        }
    }
}
