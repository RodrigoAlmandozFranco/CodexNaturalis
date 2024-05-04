package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;

public class NicknameInvalidErrorMessage extends Message {
    private JsonObject object;

    public NicknameInvalidErrorMessage(){
        object = new JsonObject();
    }

    public String execute(){
        object.addProperty("type", "error");
        object.addProperty("message", "NicknameInvalid");
        return object.toString();
    }
}