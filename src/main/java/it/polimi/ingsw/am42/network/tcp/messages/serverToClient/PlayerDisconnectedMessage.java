package it.polimi.ingsw.am42.network.tcp.messages.serverToClient;

import it.polimi.ingsw.am42.network.tcp.messages.Message;

/**
 * Message sent by the server to the client to notify that one player has disconnected and therefore the game ends.
 * @see it.polimi.ingsw.am42.network.tcp.messages.Message
 * @see it.polimi.ingsw.am42.network.tcp.server.ClientHandler
 *
 * @author Rodrigo Almandoz Franco
 * @author Mattia Brandi
 */
public class PlayerDisconnectedMessage extends Message {

    public PlayerDisconnectedMessage() {}
}
