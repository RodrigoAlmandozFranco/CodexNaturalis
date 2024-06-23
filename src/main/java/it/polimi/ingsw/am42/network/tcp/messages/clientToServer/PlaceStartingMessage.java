package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendAvailableColorsMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.WrongTurnErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.util.List;

/**
 * Message sent by the client to the server to place a face of the Starting Card on the board.
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class PlaceStartingMessage extends Message {
    private String nickname;
    private Face face;
    public PlaceStartingMessage(String nickname, Face face) {
        this.nickname = nickname;
        this.face = face;
    }

    /**
     * This method is called by the ClientHandler, and it calls the controller to place the starting Card
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message to notify that everything has been done correctly or a message to notify a WrongTurnException
     *
     */
    public Message execute(ClientHandler clientHandler, Controller controller) {
        try {
            List<PlayersColor> colors = controller.placeStarting(nickname, face);
            return new SendAvailableColorsMessage(colors);
        } catch (WrongTurnException e) {
            return new WrongTurnErrorMessage();
        }

    }
}
