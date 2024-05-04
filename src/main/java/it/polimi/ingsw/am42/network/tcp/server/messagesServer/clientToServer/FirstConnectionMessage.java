package it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.controller.MessageListener;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Message;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.*;

public class FirstConnectionMessage extends Message {
    private JsonObject object;
    private ClientHandler l;

    public FirstConnectionMessage(JsonObject o, ClientHandler l) {
        object = o;
        this.l = l;
    }

    public String execute() {
        String nickname = object.get("nickname").getAsString();
        int numPlayers = object.get("numPlayers").getAsInt();

        try {
            controller.createGame(l, nickname, numPlayers);
        } catch (NumberPlayerWrongException e) {
            return new NumberPlayersWrongErrorMessage().execute();
        } catch (GameFullException e) {
            return new GameFullErrorMessage().execute();
        } catch (NicknameInvalidException e){
            return new NicknameInvalidErrorMessage().execute();
        } catch (NicknameAlreadyInUseException e) {
            return new NicknameAlreadyInUseErrorMessage().execute();
        }
        return new InfoMessage().execute();
    }
}
