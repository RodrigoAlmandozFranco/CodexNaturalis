package it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.cards.types.GoalCard;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Messages;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.InfoMessage;

public class ChosenGoalMessage extends Messages {
    private JsonObject object;

    public ChosenGoalMessage(JsonObject object){
        this.object = object;
    }

    public String execute() {
        String nickname = object.get("nickname").getAsString();
        int idCard = object.get("id").getAsInt();

        GoalCard goal = game.getGoalCard(idCard);

        controller.chooseGoal(nickname, goal);

        return new InfoMessage().execute();
    }

}
