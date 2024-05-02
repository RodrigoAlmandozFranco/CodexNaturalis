package it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Messages;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.InfoMessage;

public class ChosenColorMessage extends Messages {
    private JsonObject object;

    public ChosenColorMessage(JsonObject object){
        this.object = object;
    }

    public String execute() {
        String nickname = object.get("nickname").getAsString();
        Color color = Color.valueOf(object.get("color").getAsString());

        controller.chooseColor(nickname, color);

        return new InfoMessage().execute();
    }

}
