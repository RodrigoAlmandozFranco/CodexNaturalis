package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendPossibleGoalsMessage;

import java.util.List;

/**
 * Message sent by the client to the server to choose a color
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ChosenColorMessage extends ClientToServerMessage {
    private String nickname;
    private Color color;

    public ChosenColorMessage(String n, Color c){
        nickname = n;
        color = c;
    }

    public Message execute() {
        List<GoalCard> goals = controller.chooseColor(nickname, color);
        return new SendPossibleGoalsMessage(goals);
    }
}
