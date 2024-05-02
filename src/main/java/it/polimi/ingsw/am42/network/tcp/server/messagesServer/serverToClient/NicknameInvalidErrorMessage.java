package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonObject;

public class NicknameInvalidErrorMessage{
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