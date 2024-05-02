package it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.cards.types.Face;
import it.polimi.ingsw.am42.model.exceptions.RequirementsNotMetException;
import it.polimi.ingsw.am42.model.structure.Position;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.NoRequirementsErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Messages;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.InfoMessage;

public class PlaceMessage extends Messages {
    private JsonObject object;

    public PlaceMessage(JsonObject object){
        this.object = object;
    }

    public String execute(){

        int idGame = object.get("id").getAsInt();
        String nickname = object.get("nickname").getAsString();

        Face face = game.getFace(object.get("srcImage").getAsString());
        int x = object.get("x").getAsInt();
        int y = object.get("y").getAsInt();
        Position position = new Position(x, y);

        try {
            controller.place(nickname, face, position);
        } catch (RequirementsNotMetException e) {
            return new NoRequirementsErrorMessage().execute();
        }

        return new InfoMessage().execute();

    }



}
