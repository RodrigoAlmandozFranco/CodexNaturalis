package it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.cards.types.PlayableCard;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Messages;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.InfoMessage;

public class PickMessage extends Messages {
    private JsonObject object;

    public PickMessage(JsonObject object){
        this.object = object;
    }

    @Override
    public String execute() {
        String nickname = object.get("nickname").getAsString();
        int idCard = object.get("id").getAsInt();

        PlayableCard card = game.getPlayableCard(idCard);

        controller.pick(nickname, card);

        return new InfoMessage().execute();
    }
}
