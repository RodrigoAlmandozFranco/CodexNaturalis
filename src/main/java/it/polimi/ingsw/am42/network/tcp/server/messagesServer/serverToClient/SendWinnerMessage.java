package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.Player;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;

import java.util.List;

public class SendWinnerMessage extends Message {
    private JsonObject object;

    public SendWinnerMessage(JsonObject object){
        this.object = object;
    }

    public String execute() {
        JsonArray array = new JsonArray();
        List<Player> winners = controller.getWinner();
        for(Player player : winners){
            array.add(player.getNickname());
        }

        JsonObject result = new JsonObject();
        result.add("winners", array);
        return result.toString();
    }
}
