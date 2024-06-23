package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.*;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

/**
 * Message sent by the client to the server to load and reconnect to an existing game.
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class ReconnectMessage extends Message {
    private String nickname;

    public ReconnectMessage(String n){
        nickname = n;
    }

    /**
     * This method is called by the ClientHandler, and it reconnects a Player to a loaded Game
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message to notify if everything has been done correctly or if there are
     * exceptions to be managed by the client
     *
     */
    public Message execute(ClientHandler clientHandler, Controller controller) {

        try {
            controller.reconnect(clientHandler, nickname);
        } catch (GameFullException e) {
            return new GameFullErrorMessage(e.getMessage());
        } catch (NicknameInvalidException e){
            return new NicknameInvalidErrorMessage(e.getMessage());
        } catch (NicknameAlreadyInUseException e) {
            return new NicknameAlreadyInUseErrorMessage(e.getMessage());
        } catch (GameAlreadyCreatedException e){
            return new GameAlreadyCreatedErrorMessage();
        }
        clientHandler.setNickname(nickname);
        return new GoodMessage();
    }
}
