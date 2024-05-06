package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;

/**
 * Message sent by the client to the server to pick a card
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ServerToClientMessage
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class PickMessage extends ClientToServerMessage {
    private String nickname;
    private PlayableCard card;

    public PickMessage(String n, PlayableCard c){
        nickname = n;
        card = c;
    }

    @Override
    public Message executeServer() {
        controller.pick(nickname, card);
        return null;
    }
}
