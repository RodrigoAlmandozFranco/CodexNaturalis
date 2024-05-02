package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Messages;

import java.util.Set;

public class SendAvailablePositionMessage extends Messages {
    private JsonObject object;

    public SendAvailablePositionMessage(JsonObject object){
        this.object = object;
    }

    public String execute(){
        String nickname = object.get("nickname").getAsString();

        Set<Position> positions = controller.getAvailablePositions(nickname);

        JsonArray array = new JsonArray();

        for(Position position : positions){
            JsonObject positionObject = new JsonObject();
            positionObject.addProperty("x", position.getX());
            positionObject.addProperty("y", position.getY());
            array.add(positionObject);
        }

        JsonObject result = new JsonObject();
        result.add("positions", array);

        return result.toString();
    }
}
