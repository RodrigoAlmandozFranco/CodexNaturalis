package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.enumeration.Color;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;

import java.util.List;

public class SendAvailableColorsMessage extends Message {
    private JsonObject object;


    public SendAvailableColorsMessage(JsonObject object){
        this.object = object;
    }

    public String execute(){

        List<Color> colors = controller.getAvailableColors(object.get("nickname").getAsString());
        JsonArray array = new JsonArray();

        for(Color color : colors){
            array.add(color.toString());
        }

        JsonObject result = new JsonObject();
        result.add("colors", array);

        return result.toString();

    }
}
