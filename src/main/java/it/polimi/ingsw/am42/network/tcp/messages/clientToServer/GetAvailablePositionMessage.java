package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendAvailablePositionMessage;

import java.util.Set;

/**
 * Message sent by the client to the server to get the available positions
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class GetAvailablePositionMessage extends ClientToServerMessage {
    private String nickname;

    public GetAvailablePositionMessage(String nickname) {
        this.nickname = nickname;
    }


    public Message execute(Controller controller) {
        Set<Position> positions = controller.getAvailablePositions(nickname);
        return new SendAvailablePositionMessage(positions);
    }
}
