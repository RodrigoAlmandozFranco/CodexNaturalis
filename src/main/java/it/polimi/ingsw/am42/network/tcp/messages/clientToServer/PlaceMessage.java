package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.NoRequirementsErrorMessage;

import java.util.ResourceBundle;

/**
 * Message sent by the client to the server to place a face on the board.
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class PlaceMessage extends ClientToServerMessage {
    private String nickname;
    private Face face;
    private Position position;

    public PlaceMessage(String n, Face f, Position p){
        nickname = n;
        face = f;
        position = p;
    }

    public Message execute(Controller controller) {

        try {
            controller.place(nickname, face, position);
        } catch (RequirementsNotMetException e) {
            return new NoRequirementsErrorMessage();
        }
        return new GoodMessage();
    }
}
