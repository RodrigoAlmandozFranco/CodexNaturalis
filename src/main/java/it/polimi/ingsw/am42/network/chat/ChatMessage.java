package it.polimi.ingsw.am42.network.chat;

import it.polimi.ingsw.am42.controller.Controller;
import it.polimi.ingsw.am42.network.tcp.messages.Message;

import java.io.Serializable;

/**
 * This class represents a chat message.
 * It contains the message, the sender and the receiver.
 * The receiver can be "all" if the message is for all the players.
 *
 * It extends the Message class and implements the Serializable interface.
 * In order to send the message to a client connected via TCP, it is serialized and sent.
 *
 * It has all the getters for the attributes.
 *
 * @author Rodrigo Almandoz Franco
 */

public class ChatMessage extends Message implements Serializable {
    private String message;
    private String sender;
    private String receiver;

    public ChatMessage(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
    }

    public ChatMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
        this.receiver = "all";
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }
}
