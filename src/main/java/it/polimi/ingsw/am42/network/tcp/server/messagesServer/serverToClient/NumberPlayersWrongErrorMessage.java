package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;

public class NumberPlayersWrongErrorMessage extends Message {
    private JsonObject object;

    public NumberPlayersWrongErrorMessage(){
        object = new JsonObject();
    }

    public String execute(){
        object.addProperty("type", "error");
        object.addProperty("message", "NumberPlayersWrong");
        return object.toString();
    }
}
