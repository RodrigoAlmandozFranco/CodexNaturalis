package it.polimi.ingsw.am42.network.tcp.messages.clientToServer;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.exceptions.GameAlreadyCreatedException;
import it.polimi.ingsw.am42.model.exceptions.GameFullException;
import it.polimi.ingsw.am42.model.exceptions.NicknameAlreadyInUseException;
import it.polimi.ingsw.am42.model.exceptions.NicknameInvalidException;
import it.polimi.ingsw.am42.model.exceptions.NumberPlayerWrongException;
import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.*;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

/**
 * Message sent by the client to the server to create a game and connect to it
 * The executeServer method calls a controller method in order to achieve the goal of the message
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */

public class FirstConnectionMessage extends Message {
    private String nickname;
    private int numPlayers;

    public FirstConnectionMessage(String n, int num) {
        nickname = n;
        numPlayers = num;
    }

    /**
     * This method is called by the ClientHandler, and it connects the First Player to the Game
     *
     * @param clientHandler ClientHandler unique for each client
     * @param controller controller of the Game
     *
     * @return message that notifies if everything has been done correctly or if there are
     * exceptions to be managed by the client
     *
     */
    public Message execute(ClientHandler clientHandler, Controller controller) {

        try {
            controller.createGame(clientHandler, nickname, numPlayers);
        } catch (NumberPlayerWrongException e) {
            return new NumberPlayersWrongErrorMessage(e.getMessage());
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
