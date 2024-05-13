package it.polimi.ingsw.am42.network.tcp.messages;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.model.Game;
import it.polimi.ingsw.am42.network.tcp.server.ClientHandler;

import java.io.Serializable;

/**
 * This class is the superclass of all the messages that the client sends to the server.
 * It contains the controller, the game and the clientHandler.
 * They are protected variables in order to be accessed by the subclasses.
 *
 * @author Rodrigo Almandoz Franco
 */

public class ClientToServerMessage extends Message {
    protected Controller controller;
    protected ClientHandler clientHandler;

    public ClientToServerMessage(Controller controller, ClientHandler c) {
        this.controller = controller;
        this.clientHandler = c;
    }

    protected ClientToServerMessage(){}
}
