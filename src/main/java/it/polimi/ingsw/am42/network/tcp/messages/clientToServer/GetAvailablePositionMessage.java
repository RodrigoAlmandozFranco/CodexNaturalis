package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.WrongTurnException;
import it.polimi.ingsw.am42.model.enumeration.PlayersColor;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendAvailableColorsMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendAvailablePositionMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.WrongTurnErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.util.List;
import java.util.Set;

/**
 * Message sent by the client to the server to get the available positions
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class GetAvailablePositionMessage extends Message {
    private String nickname;

    public GetAvailablePositionMessage(String nickname) {
        this.nickname = nickname;
    }


    public Message execute(ClientHandler clientHandler, Controller controller) {
        try {
            Set<Position> positions = controller.getAvailablePositions(nickname);
            return new SendAvailablePositionMessage(positions);
        } catch (WrongTurnException e) {
            return new WrongTurnErrorMessage();
        }
    }
}
