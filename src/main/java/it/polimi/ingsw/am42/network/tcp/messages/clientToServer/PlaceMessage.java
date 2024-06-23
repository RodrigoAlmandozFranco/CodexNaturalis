package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.NoRequirementsErrorMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.WrongTurnErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

/**
 * Message sent by the client to the server to place a face on the board.
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class PlaceMessage extends Message {
    private String nickname;
    private Face face;
    private Position position;

    public PlaceMessage(String n, Face f, Position p){
        nickname = n;
        face = f;
        position = p;
    }

    /**
     * This method is called by the ClientHandler, and it calls the controller to place the chosen Card
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message to notify that everything has been done correctly or a message to notify a WrongTurnException
     * or a RequirementsNotMetException
     *
     */
    public Message execute(ClientHandler clientHandler, Controller controller) {

        try {
            controller.place(nickname, face, position);
        } catch (RequirementsNotMetException e) {
            return new NoRequirementsErrorMessage();
        } catch (WrongTurnException e) {
            return new WrongTurnErrorMessage();
        }
        return new GoodMessage();
    }
}
