package it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Messages;

public class ReconnectMessage extends Messages {
    private JsonObject object;

    public ReconnectMessage(JsonObject object){
        this.object = object;
    }

    public String execute() {
        String nickname = object.get("nickname").getAsString();
        controller.reconnect(null, nickname, null);
        return null;
    }

}
