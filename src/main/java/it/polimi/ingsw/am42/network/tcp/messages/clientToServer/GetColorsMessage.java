package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendAvailableColorsMessage;

import java.util.List;

/**
 * Message sent by the client to the server to get the colors available
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class GetColorsMessage extends ClientToServerMessage {
    private String nickname;

    public GetColorsMessage(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public Message execute() {
        List<Color> colors = controller.getAvailableColors(nickname);
        return new SendAvailableColorsMessage(colors);
    }
}
