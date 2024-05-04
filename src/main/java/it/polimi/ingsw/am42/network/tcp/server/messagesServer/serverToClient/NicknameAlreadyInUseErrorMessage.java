package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;

public class NicknameAlreadyInUseErrorMessage extends Message {
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
