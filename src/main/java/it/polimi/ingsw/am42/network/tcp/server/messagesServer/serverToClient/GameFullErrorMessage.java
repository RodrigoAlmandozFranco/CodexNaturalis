package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;


import com.google.gson.JsonObject;

public class GameFullErrorMessage {
    private JsonObject object;

    public GameFullErrorMessage(){
        object = new JsonObject();
    }

    public String execute(){
        object.addProperty("type", "error");
        object.addProperty("message", "GameFull");
        return object.toString();
    }
}
