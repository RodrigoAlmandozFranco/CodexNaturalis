package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.SendAvailableColorsMessage;

import java.util.List;

public class PlaceStartingMessage extends ClientToServerMessage {
    private String nickname;
    private Face face;
    public PlaceStartingMessage(String nickname, Face face) {
        this.nickname = nickname;
        this.face = face;
    }

    public Message execute(Controller controller) {
        List<Color> colors = controller.placeStarting(nickname, face);
        return new SendAvailableColorsMessage(colors);
    }
}
