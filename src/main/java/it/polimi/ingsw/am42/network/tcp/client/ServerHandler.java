package it.polimi.ingsw.am42.network.tcp.client;

import it.polimi.ingsw.am42.network.tcp.messages.Message;
import it.polimi.ingsw.am42.network.tcp.messages.serverToClient.ChangeMessage;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ServerHandler implements Runnable {
    private Message message;
    private ObjectInputStream input;
    private boolean newMessage = false;
    private ClientTCP client;

    public ServerHandler(ObjectInputStream input, ClientTCP client) {
        this.input = input;
        this.client = client;
        message = null;
    }

    @Override
    public void run() {
        try {
            if (input.available() > 0 && !newMessage) {
                Message message = (Message) input.readObject();
                if(message instanceof ChangeMessage){
                    client.update(((ChangeMessage) message).getChange());
                } else
                    newMessage = true;
            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Message getMessage () {
        if (newMessage) {
            newMessage = false;
            return message;
        }
        else
            return null;
    }
}

