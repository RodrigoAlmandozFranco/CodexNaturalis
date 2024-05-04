package it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;

import java.util.List;

public class SendPossibleGoalsMessage extends Message {
    private JsonObject object;

    public SendPossibleGoalsMessage(JsonObject object){
        this.object = object;
    }

    public String execute(){

        String nickname = object.get("nickname").getAsString();

        List<GoalCard> goals = controller.getGoals(nickname);

        JsonArray array = new JsonArray();

        for(GoalCard goal : goals){
            JsonObject goalObject = new JsonObject();
            goalObject.addProperty("id", goal.getId());
            array.add(goalObject);
        }
        JsonObject result = new JsonObject();

        result.add("goals", array);

        return result.toString();
    }
}
