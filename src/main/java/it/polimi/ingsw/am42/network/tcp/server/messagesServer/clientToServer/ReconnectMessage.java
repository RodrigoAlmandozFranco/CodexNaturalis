package it.polimi.ingsw.am42.network.tcp.server.messagesServer.clientToServer;

import com.google.gson.JsonObject;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.Messages;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.GameFullErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.NicknameAlreadyInUseErrorMessage;
import it.polimi.ingsw.am42.network.tcp.server.messagesServer.serverToClient.NicknameInvalidErrorMessage;

public class ReconnectMessage extends Messages {
    private JsonObject object;
    private ClientHandler l;

    public ReconnectMessage(JsonObject object, ClientHandler l){
        this.object = object;
        this.l = l;
    }

    public String execute() {
        String nickname = object.get("nickname").getAsString();
        int idGame = object.get("id").getAsInt();

        try {
            controller.reconnect(l, nickname, idGame);
        } catch (GameFullException e) {
            return new GameFullErrorMessage().execute();
        } catch (NicknameInvalidException e){
            return new NicknameInvalidErrorMessage().execute();
        } catch (NicknameAlreadyInUseException e) {
            return new NicknameAlreadyInUseErrorMessage().execute();
        }
        return null;
    }

}
