package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonObject;

public class NoRequirementsErrorMessage {
    private JsonObject object;

    public NoRequirementsErrorMessage(){
        object = new JsonObject();
    }

    public String execute(){
        object.addProperty("type", "error");
        object.addProperty("message", "NoRequirements");
        return object.toString();
    }
}
