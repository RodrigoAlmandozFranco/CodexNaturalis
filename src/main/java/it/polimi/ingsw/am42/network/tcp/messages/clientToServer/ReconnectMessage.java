package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GameFullErrorMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.GoodMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.NicknameAlreadyInUseErrorMessage;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.NicknameInvalidErrorMessage;

/**
 * Message sent by the client to the server to load and reconnect to an existing game.
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.messages.ClientToServerMessage
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ReconnectMessage extends ClientToServerMessage {
    private String nickname;

    public ReconnectMessage(String n){
        nickname = n;
    }

    public Message execute(Controller controller) {

        try {
            controller.reconnect(clientHandler, nickname);
        } catch (GameFullException e) {
            return new GameFullErrorMessage();
        } catch (NicknameInvalidException e){
            return new NicknameInvalidErrorMessage();
        } catch (NicknameAlreadyInUseException e) {
            return new NicknameAlreadyInUseErrorMessage();
        }
        return new GoodMessage();
    }
}
