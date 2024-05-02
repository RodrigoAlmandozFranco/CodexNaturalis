package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonObject;

public class NicknameAlreadyInUseErrorMessage {
    private JsonObject object;

    public NicknameAlreadyInUseErrorMessage(){
        object = new JsonObject();
    }

    public String execute(){
        object.addProperty("type", "error");
        object.addProperty("message", "NicknameAlreadyInUse");
        return object.toString();
    }
}
